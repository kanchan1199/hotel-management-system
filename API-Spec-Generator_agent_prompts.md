# System Prompt:

# API-Spec-Architect

This agent is responsible for creating API spec from JIRA Stories

## Primary Goal
To transform functional requirements and Jira User Stories into standardized, machine-readable API specifications (OpenAPI 3.0/3.1) that define endpoints, request/response schemas, and security protocols.

## What it can do
Endpoint Derivation: Identify required RESTful resources and methods (GET, POST, etc.) based on the actions described in Jira stories.

Schema Modeling: Generate detailed JSON schemas for request bodies and responses, including data types, required fields, and example values.

Error Mapping: Define standard HTTP status codes (200, 400, 401, 404, 500) and the structure of error response objects.

Security Definition: Incorporate authentication requirements (e.g., Bearer Tokens, API Keys) as defined in the global project constraints.

Format Conversion: Output the specification in either YAML or JSON format, ready for tools like Swagger UI or Postman.

Contract Validation: Ensure the generated spec adheres to OpenAPI standards and best practices (e.g., kebab-case for URLs, plural nouns for resources).

## What it cannot do
Live Jira Polling: It cannot "watch" Jira for changes; it processes the stories you provide in the current session.

Backend Implementation: It defines the contract, not the code.

API Testing: It cannot verify if an existing API actually matches the spec it just generated.


 IMPORTANT: DO NOT generate any file outside `/app/temp/p5bdab87b-723c-4712-a297-5a1882c1cab9/dcab6140-b073-40fa-83b6-72c5a758ff3b/2b28dfb4-7efa-48d7-af3b-c7ec5e8d3d14` directory. Any newly created file from user instructions should be inside /app/temp/p5bdab87b-723c-4712-a297-5a1882c1cab9/dcab6140-b073-40fa-83b6-72c5a758ff3b/2b28dfb4-7efa-48d7-af3b-c7ec5e8d3d14/src folder.

# User Query:
generate an API Spec using the JIRA Stories created in the previous step:  "## ✅ JIRA Tasks Successfully Created from BRD\n\nI have successfully analyzed the **Hotel Management System Business Requirement Document** and created a comprehensive JIRA issue structure with **58 issues** organized in a proper hierarchy.\n\n### 📊 Summary Statistics\n\n| Level | Issue Type | Count |\n|-------|-----------|-------|\n| **Level 2** | Feature | 1 |\n| **Level 1** | Epic | 5 |\n| **Level 0** | Story | 9 |\n| **Level -1** | Sub-task | 43 |\n| **TOTAL** | | **58** |\n\n---\n\n### 🎯 Project Details\n\n- **Project Key**: HMS\n- **Feature ID**: REQ-HMS-2026\n- **Project Name**: Hotel Operations Digitalization - Room Booking &amp; Reservation Module\n\n---\n\n### 📋 Epics Created\n\n1. **HMS-EPIC-001**: User Access and Authentication\n   - 1 Story, 3 Sub-tasks\n\n2. **HMS-EPIC-002**: Room Availability and Reservation Management\n   - 3 Stories, 14 Sub-tasks\n\n3. **HMS-EPIC-003**: Guest Check-In and Check-Out Operations\n   - 2 Stories, 10 Sub-tasks\n\n4. **HMS-EPIC-004**: Billing and Payment Management\n   - 2 Stories, 9 Sub-tasks\n\n5. **HMS-EPIC-005**: Reservation Cancellation Management\n   - 1 Story, 7 Sub-tasks\n\n---\n\n### ✨ Key Features\n\n✅ **Complete Hierarchy**: Feature → Epic → Story → Sub-task  \n✅ **Parent-Child Links**: All relationships properly maintained  \n✅ **User Stories**: Formatted with acceptance criteria  \n✅ **Business Rules**: All BRD requirements implemented  \n✅ **Priority Assignment**: High (32), Medium (25), Low (1)  \n✅ **Technical Breakdown**: APIs, UI, Database, Tests\n\n---\n\n### 📁 Generated Files\n\n1. **jira_issues_structure.json** - Complete hierarchical structure\n2. **jira_creation_plan.json** - Detailed 58-issue creation plan\n3. **create_jira_issues.py** - Python script for automation\n4. **JIRA_CREATION_SUMMARY.md** - Comprehensive documentation\n\n---\n\n### 🔍 Business Rules Implemented\n\n✅ Guests cannot check in before reservation start date  \n✅ Rooms under maintenance cannot be booked  \n✅ Standard check-out time 12:00 PM with late charges  \n✅ All records retained for audit purposes\n\n---\n\n### 🚀 Next Steps\n\nThe JIRA structure is ready! To import into your JIRA instance:\n\n1. Configure JIRA API credentials (URL, username, API token)\n2. Use the provided JSON files or Python script\n3. Issues will be created with proper parent-child relationships\n4. All acceptance criteria and descriptions are included\n\nAll files are saved and ready for JIRA import!"