## Architecture du banc de test

`​``mermaid
flowchart LR
    Client[k6] --> API[Spring Boot]
    API --> DB[(H2)]
`​``