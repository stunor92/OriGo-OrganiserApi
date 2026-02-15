# Event Entry Sync API

This API service fetches event entry lists from the origo-eventor-api and saves signed up competitors to the Supabase PostgreSQL database.

## Overview

Based on the `getEventEntryList` functionality from [origo-eventor-api](https://github.com/stunor92/origo-eventor-api), this service provides an endpoint to sync competitor entries from external event systems into the local database.

**Key Differences from eventor-api:**
- Does not return competitors in REST response
- Saves signed up competitors directly to database
- Focuses only on entry list (not result list or start list)

## API Endpoint

### POST `/rest/event/{eventorId}/{eventId}/{raceId}/sync-entries`

**Note:** The `/rest` prefix comes from the application's servlet context-path configured in `application.yml`. The controller is mapped to `/event`, so the full path becomes `/rest/event/...`.

Syncs event entry list from eventor-api and saves signed up competitors to the database.

**Path Parameters:**
- `eventorId` (String) - The eventor organization ID
- `eventId` (String) - The event ID
- `raceId` (String) - The race ID

**Response:**
```json
{
  "message": "Successfully synced competitors",
  "count": 42,
  "eventorId": "1",
  "eventId": "123",
  "raceId": "456"
}
```

**Status Codes:**
- `200 OK` - Competitors synced successfully
- `500 Internal Server Error` - Error syncing competitors

## Configuration

### Environment Variables

Add the following environment variable to configure the eventor-api base URL:

```bash
EVENTOR_API_BASE_URL=https://eventor-api.example.com/rest
```

Or in your deployment platform (e.g., Supabase, Docker):

```yaml
environment:
  EVENTOR_API_BASE_URL: https://eventor-api.example.com/rest
```

**Default:** `http://localhost:8081/rest`

### Database Setup

Before using this API, you must create the `competitor` table in your PostgreSQL/Supabase database.

Run the migration script:

```bash
psql -h <host> -U <username> -d <database> -f db/migrations/001_create_competitor_table.sql
```

Or copy the content of `db/migrations/001_create_competitor_table.sql` into your Supabase SQL Editor.

## Architecture

### Components

1. **EventController** (`controller/EventController.kt`)
   - REST endpoint for syncing event entries
   - Path: `/event/{eventorId}/{eventId}/{raceId}/sync-entries`

2. **EventEntryService** (`services/EventEntryService.kt`)
   - Fetches entry list from eventor-api using RestTemplate
   - Converts entries to PersonCompetitor models
   - Saves competitors to database via repository

3. **CompetitorRepository** (`data/CompetitorRepository.kt`)
   - JDBC-based repository for database operations
   - Supports upsert (insert or update) operations
   - Queries by race ID

4. **Data Models**
   - `PersonEntry` - Entry data from eventor-api
   - `PersonCompetitor` - Competitor data for database storage
   - `EntryStatus` / `CompetitorStatus` - Status enumerations

### Data Flow

```
eventor-api → EventEntryService → CompetitorRepository → PostgreSQL
     ↓              ↓                      ↓
  Entry List   Conversion            Save to DB
```

## Usage Example

### Using cURL

```bash
curl -X POST "http://localhost:8080/rest/event/1/123/456/sync-entries" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN"
```

### Using JavaScript/Fetch

```javascript
const response = await fetch(
  'http://localhost:8080/rest/event/1/123/456/sync-entries',
  {
    method: 'POST',
    headers: {
      'Authorization': 'Bearer YOUR_JWT_TOKEN'
    }
  }
);

const result = await response.json();
console.log(`Synced ${result.count} competitors`);
```

## Database Schema

### competitor Table

| Column | Type | Description |
|--------|------|-------------|
| id | VARCHAR(255) | Primary key, entry ID from eventor-api |
| race_id | VARCHAR(255) | Race identifier |
| event_class_id | VARCHAR(255) | Event class identifier |
| person_id | VARCHAR(255) | Person identifier from eventor |
| given_name | VARCHAR(255) | First name |
| family_name | VARCHAR(255) | Last name |
| organisation_id | VARCHAR(255) | Organization ID |
| organisation_name | VARCHAR(255) | Organization name |
| organisation_type | VARCHAR(50) | Organization type (Club, etc.) |
| organisation_country | VARCHAR(3) | Country code |
| birth_year | INTEGER | Birth year |
| nationality | VARCHAR(3) | Nationality code |
| gender | VARCHAR(20) | Gender |
| punching_unit_id | VARCHAR(50) | E-card ID |
| punching_unit_type | VARCHAR(50) | E-card type (Emit, etc.) |
| bib | VARCHAR(50) | Bib number |
| status | VARCHAR(50) | Competitor status |
| start_time | TIMESTAMP | Start time |
| finish_time | TIMESTAMP | Finish time |
| created_at | TIMESTAMP | Record creation timestamp |
| updated_at | TIMESTAMP | Last update timestamp |

**Indexes:**
- `idx_competitor_race_id` on `race_id`
- `idx_competitor_person_id` on `person_id`
- `idx_competitor_event_class_id` on `event_class_id`

## Security

This API endpoint is protected by Spring Security with OAuth2 JWT authentication (Supabase).

Ensure the JWT token has appropriate permissions to access the endpoint.

## Error Handling

- If the eventor-api is unavailable, an empty list is returned and logged
- Individual competitor save errors are logged but don't stop the process
- Transaction rollback occurs if critical errors happen during the sync

## Development

### Build

```bash
./mvnw clean compile
```

### Test

```bash
./mvnw test
```

### Run Locally

```bash
export EVENTOR_API_BASE_URL=http://localhost:8081/rest
export POSTGRES_DB=jdbc:postgresql://localhost:54322/postgres
export POSTGRES_USER=postgres
export POSTGRES_PASSWORD=postgres
./mvnw spring-boot:run
```

## Future Enhancements

Potential improvements:
- Support for team entries (currently only person entries)
- Bulk sync for multiple races
- Incremental updates (only changed entries)
- Webhook support for automatic syncing
- Result list integration (currently excluded per requirements)
