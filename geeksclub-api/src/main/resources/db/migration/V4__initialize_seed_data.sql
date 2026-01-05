INSERT INTO users (id, full_name, username, email, password, role, created_at, updated_at)
VALUES
    -- Regular Users
    (1, 'Siva Katamreddy', 'siva', 'siva@gmail.com', '$2a$10$mk1.OKxiHA84T7Np6U3x5uJt2bdywFYiIKEztVfqcemcKtzZS2NGS', 'USER', '2024-12-01 10:00:00+00', '2024-12-01 10:00:00+00'),
    (2, 'Jane Smith', 'janesmith', 'jane.smith@example.com', '$2a$10$W.91iMpN6eYPlf04RMUhMO9Qg2jMEs5LjsSbnlLFf9Wl.uDI2bm8G', 'USER', '2024-12-02 11:30:00+00', '2024-12-02 11:30:00+00'),
    (3, 'Tech Guru', 'techguru', 'tech.guru@example.com', '$2a$10$W.91iMpN6eYPlf04RMUhMO9Qg2jMEs5LjsSbnlLFf9Wl.uDI2bm8G', 'USER', '2024-12-03 09:15:00+00', '2024-12-03 09:15:00+00'),
    (4, 'Code Ninja', 'codeninja', 'code.ninja@example.com', '$2a$10$W.91iMpN6eYPlf04RMUhMO9Qg2jMEs5LjsSbnlLFf9Wl.uDI2bm8G', 'USER', '2024-12-04 14:20:00+00', '2024-12-04 14:20:00+00'),
    (5, 'Alice Johnson', 'alicej', 'alice.johnson@example.com', '$2a$10$W.91iMpN6eYPlf04RMUhMO9Qg2jMEs5LjsSbnlLFf9Wl.uDI2bm8G', 'USER', '2024-12-05 08:45:00+00', '2024-12-05 08:45:00+00'),
    (6, 'Bob Williams', 'bobw', 'bob.williams@example.com', '$2a$10$W.91iMpN6eYPlf04RMUhMO9Qg2jMEs5LjsSbnlLFf9Wl.uDI2bm8G', 'USER', '2024-12-06 16:30:00+00', '2024-12-06 16:30:00+00'),
    (7, 'Sarah Connor', 'sarahc', 'sarah.connor@example.com', '$2a$10$W.91iMpN6eYPlf04RMUhMO9Qg2jMEs5LjsSbnlLFf9Wl.uDI2bm8G', 'USER', '2024-12-07 10:10:00+00', '2024-12-07 10:10:00+00'),
    (8, 'Mike Ross', 'mikeross', 'mike.ross@example.com', '$2a$10$W.91iMpN6eYPlf04RMUhMO9Qg2jMEs5LjsSbnlLFf9Wl.uDI2bm8G', 'USER', '2024-12-08 13:25:00+00', '2024-12-08 13:25:00+00'),
    (9, 'Emma Watson', 'emmaw', 'emma.watson@example.com', '$2a$10$W.91iMpN6eYPlf04RMUhMO9Qg2jMEs5LjsSbnlLFf9Wl.uDI2bm8G', 'USER', '2024-12-09 11:50:00+00', '2024-12-09 11:50:00+00'),
    (10, 'David Chen', 'davidchen', 'david.chen@example.com', '$2a$10$W.91iMpN6eYPlf04RMUhMO9Qg2jMEs5LjsSbnlLFf9Wl.uDI2bm8G', 'USER', '2024-12-10 15:00:00+00', '2024-12-10 15:00:00+00'),

    -- Admin User
    (100, 'Admin User', 'admin', 'admin@gmail.com', '$2a$10$3QkfRh4wc3h9V6wtAOG6Xu.lZ9Q8WKQK5wD5Gbph96I6mW7w0lrla', 'ADMIN', '2024-11-01 08:00:00+00', '2024-11-01 08:00:00+00');

INSERT INTO messages (id, user_id, content, is_spam, spam_confidence, status, created_at, updated_at)
VALUES
    -- Recent tech news and discussions
    (1, 1, 'Just discovered Spring AI! The integration with OpenAI and other LLM providers is incredibly smooth. Building intelligent applications has never been easier. #SpringAI #Java #AI', false, 0.0123, 'PUBLISHED', '2025-01-05 10:30:00+00', '2025-01-05 10:30:00+00'),

    (2, 2, 'Why do programmers prefer dark mode? Because light attracts bugs! #DevHumor #Programming', false, 0.0089, 'PUBLISHED', '2025-01-05 09:15:00+00', '2025-01-05 09:15:00+00'),

    (3, 3, 'PostgreSQL 16 introduced some amazing performance improvements. The query parallelism enhancements are game-changing for large datasets. Anyone else upgraded yet? #PostgreSQL #Database', false, 0.0156, 'PUBLISHED', '2025-01-05 08:45:00+00', '2025-01-05 08:45:00+00'),

    (4, 4, 'Hot take: Microservices are often over-engineered. For most applications, a well-structured monolith with clear module boundaries is more than sufficient. Don''t let architecture astronauts convince you otherwise. #SoftwareArchitecture', false, 0.0234, 'PUBLISHED', '2025-01-04 16:20:00+00', '2025-01-04 16:20:00+00'),

    (5, 5, 'Kafka or RabbitMQ? After using both in production, I''ve learned that the answer is: it depends. Kafka wins for event streaming and high throughput, RabbitMQ for traditional messaging patterns. #Kafka #RabbitMQ #MessageQueue', false, 0.0167, 'PUBLISHED', '2025-01-04 14:10:00+00', '2025-01-04 14:10:00+00'),

    (6, 6, 'Just finished reading "Clean Architecture" by Uncle Bob. Mind blown! >/ The dependency rule and the way he explains architectural boundaries is brilliant. Highly recommended for any serious developer. #CleanCode #SoftwareEngineering', false, 0.0145, 'PUBLISHED', '2025-01-04 11:30:00+00', '2025-01-04 11:30:00+00'),

    (7, 7, 'Angular 18 is here! The new signals-based reactivity is a game changer. Finally ditching NgRx in favor of the built-in state management. The DX improvements are incredible. #Angular #WebDev', false, 0.0178, 'PUBLISHED', '2025-01-04 09:00:00+00', '2025-01-04 09:00:00+00'),

    (8, 8, 'Docker tip: Use multi-stage builds to keep your images lean. My Spring Boot image went from 600MB to 180MB just by properly structuring the Dockerfile. #Docker #DevOps', false, 0.0201, 'PUBLISHED', '2025-01-03 15:45:00+00', '2025-01-03 15:45:00+00'),

    (9, 9, 'TIL: You can use Java Records with Spring Data JPA for read-only projections. Much cleaner than creating separate DTO classes for every query! #Java #SpringBoot #TIL', false, 0.0134, 'PUBLISHED', '2025-01-03 13:20:00+00', '2025-01-03 13:20:00+00'),

    (10, 10, 'Testcontainers is a must-have for integration testing. Spinning up real PostgreSQL, Kafka, and Redis containers for tests has made our test suite so much more reliable. #Testing #Java', false, 0.0189, 'PUBLISHED', '2025-01-03 10:15:00+00', '2025-01-03 10:15:00+00'),

    (11, 1, 'Remember: There are only two hard things in Computer Science: cache invalidation and naming things. And off-by-one errors. = #ProgrammingHumor', false, 0.0098, 'PUBLISHED', '2025-01-02 16:30:00+00', '2025-01-02 16:30:00+00'),

    (12, 2, 'JWT authentication in Spring Security has gotten so much better with Spring Security 6. The new SecurityFilterChain DSL is much more readable. #SpringSecurity #JWT', false, 0.0167, 'PUBLISHED', '2025-01-02 14:00:00+00', '2025-01-02 14:00:00+00'),

    (13, 3, 'Flyway vs Liquibase? I''ve used both, and honestly, Flyway''s simplicity wins for me. SQL-based migrations are easier to review and understand. #DatabaseMigration #Flyway', false, 0.0145, 'PUBLISHED', '2025-01-02 11:20:00+00', '2025-01-02 11:20:00+00'),

    (14, 4, 'Debugging a distributed system is like being a detective in a crime movie, except you''re also the criminal, the victim, and the confused bystander. =u #DistributedSystems #DevLife', false, 0.0112, 'PUBLISHED', '2025-01-02 09:45:00+00', '2025-01-02 09:45:00+00'),

    (15, 5, 'Pro tip: Use @Transactional(readOnly = true) for read operations in Spring. It can improve performance by allowing the persistence provider to optimize. #SpringBoot #Performance', false, 0.0178, 'PUBLISHED', '2025-01-01 17:00:00+00', '2025-01-01 17:00:00+00'),

    (16, 6, 'Tailwind CSS vs Bootstrap? After switching to Tailwind, I can''t go back. The utility-first approach just makes so much sense once you get used to it. #TailwindCSS #WebDev', false, 0.0156, 'PUBLISHED', '2025-01-01 14:30:00+00', '2025-01-01 14:30:00+00'),

    (17, 7, 'The best code is no code at all. Before adding a new library or framework, ask yourself: do I really need this? #KISS #SimplicitY', false, 0.0134, 'PUBLISHED', '2025-01-01 11:15:00+00', '2025-01-01 11:15:00+00'),

    (18, 8, 'GraphQL vs REST: I''ve built production apps with both. REST is perfectly fine for most APIs. GraphQL shines when you have complex, nested data requirements. #GraphQL #REST #API', false, 0.0189, 'PUBLISHED', '2024-12-31 15:20:00+00', '2024-12-31 15:20:00+00'),

    (19, 9, 'Git tip: Use "git commit --fixup" for small fixes to previous commits, then "git rebase -i --autosquash" to automatically squash them. Your commit history will thank you! #Git #DevTips', false, 0.0167, 'PUBLISHED', '2024-12-31 12:00:00+00', '2024-12-31 12:00:00+00'),

    (20, 10, 'Java 21 LTS is amazing! Virtual threads (Project Loom) are a game changer for concurrent programming. Writing high-performance concurrent code has never been easier. #Java21 #VirtualThreads', false, 0.0201, 'PUBLISHED', '2024-12-31 09:30:00+00', '2024-12-31 09:30:00+00'),

    (21, 1, 'Code review best practice: Praise in public, criticize in private. Be kind and constructive. We''re all learning and growing together. #CodeReview #TeamWork', false, 0.0145, 'PUBLISHED', '2024-12-30 16:45:00+00', '2024-12-30 16:45:00+00'),

    (22, 2, 'Elasticsearch is overkill for most search use cases. PostgreSQL full-text search is incredibly powerful and eliminates an entire service from your stack. #PostgreSQL #Search', false, 0.0178, 'PUBLISHED', '2024-12-30 13:10:00+00', '2024-12-30 13:10:00+00'),

    (23, 3, 'Why do Java developers wear glasses? Because they don''t C#! = #ProgrammingJokes #Java', false, 0.0089, 'PUBLISHED', '2024-12-30 10:20:00+00', '2024-12-30 10:20:00+00'),

    (24, 4, 'GitHub Actions has become incredibly powerful. For most projects, you don''t need Jenkins anymore. The YAML-based workflows are much easier to maintain. #CI #GitHubActions', false, 0.0167, 'PUBLISHED', '2024-12-29 15:30:00+00', '2024-12-29 15:30:00+00'),

    (25, 5, 'Learning Kubernetes? Start with Docker Compose. Understanding containers well before diving into orchestration will save you tons of headaches. #Kubernetes #Docker', false, 0.0189, 'PUBLISHED', '2024-12-29 12:15:00+00', '2024-12-29 12:15:00+00'),

    -- Spam example (flagged)
    (26, 6, 'CLICK HERE NOW!!! Buy cheap software licenses! Amazing deals! Limited time offer! Don''t miss out! Visit www.scamsite.com NOW!!!', true, 0.9876, 'FLAGGED', '2024-12-29 09:00:00+00', '2024-12-29 09:00:00+00'),

    (27, 7, 'The three virtues of a programmer: laziness, impatience, and hubris. - Larry Wall. Write code that makes you lazy! #ProgrammingWisdom', false, 0.0123, 'PUBLISHED', '2024-12-28 14:20:00+00', '2024-12-28 14:20:00+00'),

    (28, 8, 'TypeScript has completely changed how I write JavaScript. The type safety catches so many bugs before runtime. Cannot imagine going back to vanilla JS. #TypeScript #JavaScript', false, 0.0156, 'PUBLISHED', '2024-12-28 11:00:00+00', '2024-12-28 11:00:00+00'),

    (29, 9, 'Pair programming is underrated. Yes, it feels slower at first, but the code quality improvement and knowledge sharing are absolutely worth it. #PairProgramming #Agile', false, 0.0178, 'PUBLISHED', '2024-12-28 08:30:00+00', '2024-12-28 08:30:00+00'),

    (30, 10, 'Maven vs Gradle? I prefer Maven for its simplicity and convention over configuration. Gradle is more flexible but that flexibility often leads to complex build files. #Maven #Gradle', false, 0.0167, 'PUBLISHED', '2024-12-27 15:45:00+00', '2024-12-27 15:45:00+00');


INSERT INTO votes (id, user_id, message_id, vote_type, created_at, updated_at)
VALUES
    -- Message 1 (Spring AI) - highly upvoted
    (1, 2, 1, 'UP', '2025-01-05 10:35:00+00', '2025-01-05 10:35:00+00'),
    (2, 3, 1, 'UP', '2025-01-05 10:40:00+00', '2025-01-05 10:40:00+00'),
    (3, 4, 1, 'UP', '2025-01-05 10:45:00+00', '2025-01-05 10:45:00+00'),
    (4, 5, 1, 'UP', '2025-01-05 11:00:00+00', '2025-01-05 11:00:00+00'),
    (5, 6, 1, 'UP', '2025-01-05 11:15:00+00', '2025-01-05 11:15:00+00'),
    (6, 7, 1, 'UP', '2025-01-05 11:30:00+00', '2025-01-05 11:30:00+00'),
    (7, 8, 1, 'UP', '2025-01-05 12:00:00+00', '2025-01-05 12:00:00+00'),
    (8, 9, 1, 'UP', '2025-01-05 12:30:00+00', '2025-01-05 12:30:00+00'),

    -- Message 2 (Programmer joke) - popular
    (9, 1, 2, 'UP', '2025-01-05 09:20:00+00', '2025-01-05 09:20:00+00'),
    (10, 3, 2, 'UP', '2025-01-05 09:25:00+00', '2025-01-05 09:25:00+00'),
    (11, 4, 2, 'UP', '2025-01-05 09:30:00+00', '2025-01-05 09:30:00+00'),
    (12, 5, 2, 'UP', '2025-01-05 09:45:00+00', '2025-01-05 09:45:00+00'),
    (13, 6, 2, 'UP', '2025-01-05 10:00:00+00', '2025-01-05 10:00:00+00'),

    -- Message 3 (PostgreSQL) - good engagement
    (14, 1, 3, 'UP', '2025-01-05 08:50:00+00', '2025-01-05 08:50:00+00'),
    (15, 2, 3, 'UP', '2025-01-05 09:00:00+00', '2025-01-05 09:00:00+00'),
    (16, 4, 3, 'UP', '2025-01-05 09:15:00+00', '2025-01-05 09:15:00+00'),
    (17, 5, 3, 'UP', '2025-01-05 09:30:00+00', '2025-01-05 09:30:00+00'),

    -- Message 4 (Microservices hot take) - controversial
    (18, 1, 4, 'UP', '2025-01-04 16:25:00+00', '2025-01-04 16:25:00+00'),
    (19, 2, 4, 'UP', '2025-01-04 16:30:00+00', '2025-01-04 16:30:00+00'),
    (20, 3, 4, 'UP', '2025-01-04 16:45:00+00', '2025-01-04 16:45:00+00'),
    (21, 5, 4, 'DOWN', '2025-01-04 17:00:00+00', '2025-01-04 17:00:00+00'),
    (22, 6, 4, 'DOWN', '2025-01-04 17:15:00+00', '2025-01-04 17:15:00+00'),
    (23, 7, 4, 'UP', '2025-01-04 17:30:00+00', '2025-01-04 17:30:00+00'),

    -- Message 5 (Kafka vs RabbitMQ)
    (24, 1, 5, 'UP', '2025-01-04 14:15:00+00', '2025-01-04 14:15:00+00'),
    (25, 2, 5, 'UP', '2025-01-04 14:30:00+00', '2025-01-04 14:30:00+00'),
    (26, 3, 5, 'UP', '2025-01-04 14:45:00+00', '2025-01-04 14:45:00+00'),
    (27, 4, 5, 'UP', '2025-01-04 15:00:00+00', '2025-01-04 15:00:00+00'),
    (28, 6, 5, 'UP', '2025-01-04 15:30:00+00', '2025-01-04 15:30:00+00'),

    -- Message 6 (Clean Architecture book)
    (29, 1, 6, 'UP', '2025-01-04 11:35:00+00', '2025-01-04 11:35:00+00'),
    (30, 2, 6, 'UP', '2025-01-04 11:45:00+00', '2025-01-04 11:45:00+00'),
    (31, 3, 6, 'UP', '2025-01-04 12:00:00+00', '2025-01-04 12:00:00+00'),
    (32, 4, 6, 'UP', '2025-01-04 12:15:00+00', '2025-01-04 12:15:00+00'),
    (33, 5, 6, 'UP', '2025-01-04 12:30:00+00', '2025-01-04 12:30:00+00'),
    (34, 7, 6, 'UP', '2025-01-04 13:00:00+00', '2025-01-04 13:00:00+00'),

    -- Message 7 (Angular 18)
    (35, 1, 7, 'UP', '2025-01-04 09:10:00+00', '2025-01-04 09:10:00+00'),
    (36, 2, 7, 'UP', '2025-01-04 09:20:00+00', '2025-01-04 09:20:00+00'),
    (37, 3, 7, 'UP', '2025-01-04 09:30:00+00', '2025-01-04 09:30:00+00'),
    (38, 5, 7, 'UP', '2025-01-04 10:00:00+00', '2025-01-04 10:00:00+00'),

    -- Message 8 (Docker tip)
    (39, 1, 8, 'UP', '2025-01-03 15:50:00+00', '2025-01-03 15:50:00+00'),
    (40, 2, 8, 'UP', '2025-01-03 16:00:00+00', '2025-01-03 16:00:00+00'),
    (41, 3, 8, 'UP', '2025-01-03 16:15:00+00', '2025-01-03 16:15:00+00'),
    (42, 4, 8, 'UP', '2025-01-03 16:30:00+00', '2025-01-03 16:30:00+00'),
    (43, 5, 8, 'UP', '2025-01-03 16:45:00+00', '2025-01-03 16:45:00+00'),
    (44, 6, 8, 'UP', '2025-01-03 17:00:00+00', '2025-01-03 17:00:00+00'),
    (45, 7, 8, 'UP', '2025-01-03 17:15:00+00', '2025-01-03 17:15:00+00'),

    -- Message 9 (Java Records TIL)
    (46, 1, 9, 'UP', '2025-01-03 13:25:00+00', '2025-01-03 13:25:00+00'),
    (47, 2, 9, 'UP', '2025-01-03 13:30:00+00', '2025-01-03 13:30:00+00'),
    (48, 3, 9, 'UP', '2025-01-03 13:45:00+00', '2025-01-03 13:45:00+00'),
    (49, 4, 9, 'UP', '2025-01-03 14:00:00+00', '2025-01-03 14:00:00+00'),

    -- Message 10 (Testcontainers)
    (50, 1, 10, 'UP', '2025-01-03 10:20:00+00', '2025-01-03 10:20:00+00'),
    (51, 2, 10, 'UP', '2025-01-03 10:30:00+00', '2025-01-03 10:30:00+00'),
    (52, 3, 10, 'UP', '2025-01-03 10:45:00+00', '2025-01-03 10:45:00+00'),
    (53, 4, 10, 'UP', '2025-01-03 11:00:00+00', '2025-01-03 11:00:00+00'),
    (54, 5, 10, 'UP', '2025-01-03 11:15:00+00', '2025-01-03 11:15:00+00'),
    (55, 6, 10, 'UP', '2025-01-03 11:30:00+00', '2025-01-03 11:30:00+00'),

    -- More votes for other messages (varied engagement)
    (56, 2, 11, 'UP', '2025-01-02 16:35:00+00', '2025-01-02 16:35:00+00'),
    (57, 3, 11, 'UP', '2025-01-02 16:40:00+00', '2025-01-02 16:40:00+00'),
    (58, 4, 11, 'UP', '2025-01-02 16:45:00+00', '2025-01-02 16:45:00+00'),

    (59, 1, 12, 'UP', '2025-01-02 14:05:00+00', '2025-01-02 14:05:00+00'),
    (60, 3, 12, 'UP', '2025-01-02 14:10:00+00', '2025-01-02 14:10:00+00'),

    (61, 1, 13, 'UP', '2025-01-02 11:25:00+00', '2025-01-02 11:25:00+00'),
    (62, 2, 13, 'UP', '2025-01-02 11:30:00+00', '2025-01-02 11:30:00+00'),
    (63, 4, 13, 'DOWN', '2025-01-02 11:45:00+00', '2025-01-02 11:45:00+00'),

    (64, 1, 20, 'UP', '2024-12-31 09:35:00+00', '2024-12-31 09:35:00+00'),
    (65, 2, 20, 'UP', '2024-12-31 09:40:00+00', '2024-12-31 09:40:00+00'),
    (66, 3, 20, 'UP', '2024-12-31 09:50:00+00', '2024-12-31 09:50:00+00'),
    (67, 4, 20, 'UP', '2024-12-31 10:00:00+00', '2024-12-31 10:00:00+00'),
    (68, 5, 20, 'UP', '2024-12-31 10:15:00+00', '2024-12-31 10:15:00+00'),
    (69, 6, 20, 'UP', '2024-12-31 10:30:00+00', '2024-12-31 10:30:00+00'),
    (70, 7, 20, 'UP', '2024-12-31 10:45:00+00', '2024-12-31 10:45:00+00'),
    (71, 8, 20, 'UP', '2024-12-31 11:00:00+00', '2024-12-31 11:00:00+00'),
    (72, 9, 20, 'UP', '2024-12-31 11:15:00+00', '2024-12-31 11:15:00+00'),

    -- Spam message gets downvotes
    (73, 1, 26, 'DOWN', '2024-12-29 09:05:00+00', '2024-12-29 09:05:00+00'),
    (74, 2, 26, 'DOWN', '2024-12-29 09:10:00+00', '2024-12-29 09:10:00+00'),
    (75, 3, 26, 'DOWN', '2024-12-29 09:15:00+00', '2024-12-29 09:15:00+00'),
    (76, 4, 26, 'DOWN', '2024-12-29 09:20:00+00', '2024-12-29 09:20:00+00'),
    (77, 5, 26, 'DOWN', '2024-12-29 09:25:00+00', '2024-12-29 09:25:00+00');
