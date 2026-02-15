# Database Migrations

This directory contains SQL migration scripts for the database schema.

## Running Migrations

Since this project doesn't use Flyway or Liquibase, migrations need to be applied manually to your PostgreSQL/Supabase database.

### Apply the competitor table migration:

```bash
psql -h <host> -U <username> -d <database> -f db/migrations/001_create_competitor_table.sql
```

Or in Supabase SQL Editor, copy and paste the content of the migration file.

## Migration Files

- `001_create_competitor_table.sql` - Creates the competitor table for storing signed up competitors from event entry lists

## Table Structure

### competitor
Stores person competitors who have signed up for events/races via the eventor-api.

Key columns:
- `id` - Unique identifier (matches entry ID from eventor-api)
- `race_id` - Reference to the race
- `event_class_id` - Reference to the event class
- `person_id` - Person identifier from eventor
- Name, organization, birth year, nationality, gender
- Punching unit information (e-card)
- Start/finish times and status
