# GitHub Workflow Rules

## Table of contents

1. [Sprint Procedure]
   - [Overview](#obzor)
   - [Sprint review](#review)
   - [Sprint planning](#planning)
2. [Contributing Changes](#contributing)
   - [Branch-naming](#branch-naming)
   - [Create pull request](#pull-request)
   - [Monitoring pull request](#tracking)
   - [merge-check](#merge-check)
   - [Completion](#done)
3. [Working with GitHub Projects](#description)
   - [Task creation](#ticket)
   - [Kanban board](#kanban)
   - [Sprint](#sprint)

## Sprint procedure <a name="sprint"></a>

In our team, we follow a structured sprint procedure to ensure effective collaboration and progress tracking. This document describes our sprint activities.

### Overview <a name="obzor"></a>

Our sprint process consists of two main activities: sprint review and sprint planning. These activities take place at the beginning of each week, usually on Mondays.

### Sprint review <a name="review"></a>

The sprint review serves as a retrospective of the previous sprint's accomplishments and as a planning session for the upcoming sprint. During this meeting, team members discuss their individual contributions and accomplishments from the previous sprint. The sprint review includes the following steps:

1. **Introduction and Agenda Setting.** The person responsible for the agenda and regulation of the meeting opens the meeting, sets the agenda, and ensures that the discussion continues.

2. **Individual updates.** Each team member details their contributions and work accomplished during the previous sprint. This includes successes, challenges encountered, and lessons learned.

3. **Documenting Sprint Results.** A designated team member is responsible for documenting the results of the Sprint Review. This includes summarizing the discussions, decisions made, and actions identified for the upcoming sprint.

### Sprint planning<a name="planning"></a>

After reviewing the sprint, the team begins sprint planning to determine the objectives and goals for the next sprint. Sprint planning includes the following steps:

1. **Review the Uncompleted Tasks Log.** Team members review the Uncompleted Tasks Log, which contains a list of potential tasks that need to be completed. Any uncompleted tasks from the previous sprint are also considered for inclusion in the upcoming sprint.

2. **Task Selection.** Team members collaboratively select tasks from the backlog for inclusion in the upcoming sprint based on priority, feasibility, and available resources.

3. **Task Assignment.** Tasks selected for the sprint are assigned to individual team members based on their experience and availability.

4. **Sprint goal setting.** The team collectively defines the goals and objectives of the upcoming sprint, ensuring that they are aligned with the project timeline and priorities.

## Making Changes <a name="сontributing"></a>

Before you can make changes to the `master` master branch, you must go through the following steps:
1. Create a new branch, before doing so, see the [**branch-naming**](#branch-naming) rule.
2. Create a pull request (PR) to the master branch, in github this is done by [**this**](#pull-request).
3. Start tracking the PR (see what [**cases**](#tracking), for which [**cases**](#tracking) options will work):
   - attach a PR to an existing issue,
   - or create an issue and then attach the PR to the issue,
   - or add the PR to a github project,
   - or create a github issue, add the PR to the github project and attach it to the issue.
4. Add changes\commits.
5. Request [review](https://docs.github.com/ru/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/requesting-a-pull-request-review) from developers.
6. After the review, change the names and merge message.
7. Check merge by: [**check merge**](#merge-check).
8. Group all commits (squash commits) and do the merge.
9. Check that everything is [**completed**](#done).

### Branch name <a name="branch-naming"></a>

New branches should be named according to the following scheme: `{noun}/{verb_or_noun}`. Thus,
branches for documentation should be named with the prefix `document` and a brief description of the change, separated by a hyphen.
Example: `document/change-srs` means change specification document. The following prefixes are currently allocated
the following prefixes: `feature`, `fix`, and `document`.

### Creating a pull request <a name="pull-request"></a>

A PR should be created from the new branch so that other developers know what tasks are being worked on
are being worked on. A PR can be created [like this](https://docs.github.com/ru/pull-requests/collaborating-with-pull-requests/proposing-changes-to-your-work-with-pull-requests/creating-a-pull-request).

- The header of the PR should contain a brief description of the planned changes.
- The PR description should describe the planned changes and their purpose in detail.

### Monitoring pull request <a name="tracking"></a>

Our team uses github projects to monitor all tasks. So it's important to us that
code work is visible on our board. We use issues to refer to code-related tasks, issues
can be small (small task) or large (epic). Therefore, four scenarios are described for PR monitoring.
The rules for our github projects can be found [here](#description).

1. Attach a PR to an existing issue.

>If you want to close an existing issue that is not epic (i.e., the issue has no subtasks),
then you need to attach the PR to the existing issue.

2. Create an issue and then attach the PR to the issue.

>If you want to add a change that is not represented in issues, then create a new task in github project,
then convert it to an issue. Then follow the instructions above to attach the PR to the issue.

>Another option is also possible, it is described below.

3. Add PR to github project.

>If you want to add a change that is not represented in issues, then add the PR to github projects. GitHub projects
will then add the PR to the issues board itself.

4. Create a github issue, add the PR to github projects and attach it to the issue.

>If you want to add a change that is not represented in issues, and that will be part of something big (a module,
epic), then create an issue with a description of the global task (create module, borrow epic). After that, attach
your future tasks as PRs to the new issue.


Important: when creating a PR that closes a previously created issue, add a link to that issue in the description of the PR (For example: create a module, create epic).
issue in the PR description (For example: `Closes #123` or `Fixes: #123`).

### Checking merge <a name="merge-check"></a>

The following items must be done before merge:
- get approval from the two developers,
- check the merge name against the format `{Verb} {object} {desc}``, where ``desc`` contains information on the
  issue (``Fixes #123``) that this merge closes.

### Completion <a name="done"></a>

At the end, you should check
- whether the issue is closed,
- whether PR is closed,
- whether the issue is completed in the project.

## Working with GitHub Projects <a name="description"></a>

### Task creation (Ticket)

To create a new task (Ticket) in GitHub Projects, follow these steps:
1. Open GitHub Projects and select the appropriate project.
2. Click on the "Add cards" button in the desired column to create a new task.
3. Fill in the task information:
   - **Title**: Short and descriptive title of the task.
   - **Description**: A detailed description of the task, including requirements and expected outcome.
   - **Size**: Used to estimate the complexity of the task. It can be specified as S (small), M (medium) or L (large), which corresponds to story points (1, 3, 5).
   - **Estimate**: The estimated number of hours required to complete the task.
   - **Priority**: The level of importance of the task. Can be labeled as P0 (very important), P1 (important), or P2 (less important).
   - **Other Labels**: Additional labels that can help categorize the task (e.g., task type, category, etc.).
   
### Kanban board

Our Kanban board consists of the following columns:

- **To Do**: Tasks that have not yet been started and are pending (project backlog).
- **Current Sprint**: Tasks scheduled for the current sprint.
- **In Progress**: Tasks that are currently being worked on.
- **In Review**: Tasks that have been completed and are awaiting review or revision.
- **Done**: Completed tasks that are ready to be released or sent to production.

### Sprint planning

A sprint is a fixed period of time, ours takes 2 weeks, during which the team works on a specific set of tasks. Sprint planning includes the following steps:

1. **Designation of sprint tag**: Each ticket should be labeled with the appropriate sprint tag to designate which sprint it will be completed in.
2. **Start and End of Ticket**: It is important to note the start and end date of each ticket within a sprint to keep track of progress and plan next steps.