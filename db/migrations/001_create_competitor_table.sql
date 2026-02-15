-- Competitor table to store signed up competitors from eventor-api
-- This table stores person competitors who have signed up for events/races

CREATE TABLE IF NOT EXISTS competitor (
    id VARCHAR(255) PRIMARY KEY,
    race_id VARCHAR(255) NOT NULL,
    event_class_id VARCHAR(255) NOT NULL,
    person_id VARCHAR(255),
    given_name VARCHAR(255),
    family_name VARCHAR(255),
    organisation_id VARCHAR(255),
    organisation_name VARCHAR(255),
    organisation_type VARCHAR(50),
    organisation_country VARCHAR(3),
    birth_year INTEGER,
    nationality VARCHAR(3),
    gender VARCHAR(20),
    punching_unit_id VARCHAR(50),
    punching_unit_type VARCHAR(50),
    bib VARCHAR(50),
    status VARCHAR(50) NOT NULL DEFAULT 'NotActivated',
    start_time TIMESTAMP,
    finish_time TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for common queries
CREATE INDEX IF NOT EXISTS idx_competitor_race_id ON competitor(race_id);
CREATE INDEX IF NOT EXISTS idx_competitor_person_id ON competitor(person_id);
CREATE INDEX IF NOT EXISTS idx_competitor_event_class_id ON competitor(event_class_id);

-- Create trigger to update updated_at timestamp
CREATE OR REPLACE FUNCTION update_competitor_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trigger_update_competitor_updated_at
    BEFORE UPDATE ON competitor
    FOR EACH ROW
    EXECUTE FUNCTION update_competitor_updated_at();
