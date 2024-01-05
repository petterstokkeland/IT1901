# Groupdeliverable 1

## Sprint Goal

Make a MVP “minimal viable product”. The application should solve those two user stories chosen. We wanted an app with simple GUI, domain logic and persistent storage.

## Project Methodology

The team wishes to adopt Scrum as a working framework, but acknowledges that daily meetings will not be feasible. The team has allocated two days a week for meetings, with an additional day if necessary. The scrum master will be rotated and in this sprint Petter was the Scrum master.  During these meetings, we review the work accomplished since the last gathering and outline a plan for the tasks ahead. Responsibilities are distributed, aiming to allocate approximately equal amounts of work to each member. The team is also keen on employing pair programming for tasks where it’s feasible, typically during meetings or at other times mutually agreed upon by the pair. We think pair programming could lead to overall higher knowledge in the group. Tasks are formulated based on user stories, one big part of this sprint has been on setting up the codebase and introducing a basic product feature in our application to build upon. These user stories are then broken down into more specific work requirements, using GitLab and its issue board to monitor progress. We strive to adhere to conventional commit standards, write descriptive commit messages, and create issues with clear titles and explanations. For sprint 1, we’ve established a milestone. Additionally, for sprint 1, we’ve created a branch from which we branch out and merge back into, with the intention of eventually merging the sprint 1 branch into the master branch. In this way we hope to avoid merge conflicts and be dynamic.

## Issues Solved

Every issue is categorized into: S (small), M(medium), L(large)

1. Make FXML design (S)
2. Make classes in core module (S)
3. Rename folder names and template names in folders and POM files
4. Implement classes in core (M)
5. Implement controller class (M)
6. Implement Server module and jsonController class (M)
7. Documentation (L)
   - Readme to core and ui module
   - user stories
8. Test to core module (M)
   - Expense
   - User

## Status Of The Application


The application supports user login and the creation of an expense entry. If one is a new user, a new account will be established, and the expense will be stored in JSON format associated with that user. If one already has an existing account, all expenses will be saved under the same user. As of now, it’s not possible to view the expense through the user interface; this is a feature we aim to prioritize for the next sprint. Additionally, there is no logout functionality. Moving forward, we are looking into options for sorting the display of expenses based on various parameters. 

We have implemented local file storage on your pc to ensure that our app is compatible with all types of computers. Upon installation, a folder named 'money-spender' will be created in your home directory, and within that folder, a file named 'user.json' will be generated. The 'user.json' file will be dynamically updated as you interact with the app.


## User Stories
You can find the user stories [here](user_stories.md).
