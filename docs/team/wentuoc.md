# Cheng Wentuo - Project Portfolio Page

## Overview
EZMealPlan is a CLI-based system that helps users to plan their meals. The app contains a pre-created, customisable
list of meals, which the user can filter by name, ingredients, and cost. Their favourite meals can be added into their 
personal wishlist. The app can also recommend the user a meal to prepare, based on the ingredients that they currently 
own.

### Summary of Contributions
* Code contributed: [RepoSense link](https://nus-cs2113-ay2425s2.github.io/tp-dashboard/?search=cheng%20wentuo&sort=groupTitle&sortWithin=title&timeframe=commit&mergegroup=&groupSelect=groupByRepos&breakdown=true&checkedFileTypes=docs~functional-code~test-code~other&since=2025-02-21&tabOpen=true&tabType=authorship&tabAuthor=wentuoc&tabRepo=AY2425S2-CS2113-F14-4%2Ftp%5Bmaster%5D&authorshipIsMergeGroup=false&authorshipFileTypes=docs~functional-code~test-code&authorshipIsBinaryFileTypeChecked=false&authorshipIsIgnoredFilesChecked=false)
  * Proofread and cleaned up formatting errors in the entire code
  * Added JavaDocs comments for all significant public methods
* **Significant Enhancements Implemented**:
  * Modified `Inventory` to be a HashMap to allow duplicate `Ingredients`.
  * Add checkers in `Inventory` to allow the detection of duplicate `Ingredients` for `add` and `remove`
* **Documentation**:
    * User Guide:
        * Proofread the entire UG, fixing formatting issues such as consistency across descriptions, errors in code 
          snippets, etc. (#110)
    * Developer Guide:
        * Added class diagrams for `EZMealPlan` and `CommandClass` (#100)
        * Added design details for `Food` package (#64)