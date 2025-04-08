# Ling Yijie Ryan - Project Portfolio Page

## Overview
EZMealPlan is a CLI-based system that helps users to plan their meals. Users can view a list of pre-created meals in the main
recipes list, filter them by meal name, ingredients and cost according to their personal preferences, and add them
into their personal wishlist. Users can also create their own meals which will be added into the recipes list and remove meals from their personal wishlist and the main recipes list.

### Summary of Contributions
* Code contributed: [RepoSense link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21&tabOpen=true&tabType=authorship&tabAuthor=RyanLing169&tabRepo=AY2425S2-CS2113-F14-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)
* Implemented the `ClearCommand`, allowing users to reset their inventory and wishlist with a single command.
* Documentation:
  * User Guide: Added the section describing the three types of lists (recipes, wishlist, and inventory) that the user manages throughout their usage of the application.
  * Developer Guide: Contributed the entire `DeleteCommand` subsection, including its implementation details, sequence diagram, and explanation of logic flow.
  * Developer Guide: Wrote the architecture overview for the project, providing a high-level understanding of the system's main components.
  * Developer Guide: Added several appendix sections including the glossary, product scope, non-functional requirements, and testing requirements.
* Testing:
  * Wrote and maintained `DeleteCommandTest` and `RecommendCommandTest`, ensuring valid cases and all relevant exceptions were covered.
  * Created multiple `CheckerTest` classes for validating input formats across commands like `Buy`, `Consume`, and `View`, improving overall code robustness.
