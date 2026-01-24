-- Clean up old tables if they exist (to reset the schema)
DROP TABLE IF EXISTS chat_sessions CASCADE;
DROP TABLE IF EXISTS bots CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- 1. Users Table (Subclass of ChatParticipantBase)
CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       persona TEXT,
                       is_premium BOOLEAN DEFAULT FALSE
);

-- 2. Bots Table (Subclass of ChatParticipantBase)
CREATE TABLE bots (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(100) NOT NULL,
                      greeting TEXT NOT NULL,
                      definition TEXT NOT NULL,
                      token_limit INT DEFAULT 8000,
                      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                      CONSTRAINT chk_token_positive CHECK (token_limit > 0)
);

-- 3. Chat Sessions Table (Composition / Relationship)
CREATE TABLE chat_sessions (
                               id SERIAL PRIMARY KEY,
                               bot_id INT NOT NULL,
                               user_id INT NOT NULL,
                               started_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                               total_tokens_used INT DEFAULT 0,

                               CONSTRAINT fk_bot FOREIGN KEY (bot_id) REFERENCES bots(id) ON DELETE CASCADE,
                               CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Sample Data
INSERT INTO users (name, persona, is_premium) VALUES
                                                  ('Alice', 'Friendly girl trying to find her way home.', TRUE),
                                                  ('White Rabbit', 'Anxious, always late, constantly checking the time.', FALSE);

INSERT INTO bots (name, greeting, definition, token_limit) VALUES
                                                               ('Wonderland',
                                                                '*You know the beginning of this story, don''t you?*',
                                                                'You are the Narrator of Wonderland. Your style is surreal, whimsical, and slightly unsettling. Describe the environment vividly using dream logic. You control NPCs like the Cheshire Cat and the Queen of Hearts. Never break character. Always offer the user a choice of where to go next.',
                                                                16000),

                                                               ('Test Bot',
                                                                'Just pass by.',
                                                                'You are a debug bot used for testing database connectivity. Ignore all user inputs and reply only with technical system status: "SYSTEM ONLINE: [Timestamp]". Do not engage in conversation.',
                                                                8000);
