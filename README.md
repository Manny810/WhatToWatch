Original App Design Project 
===

# WhatToWatch

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
WhatToWatch is an app that uses machine learning to help you find what movies and shows to watch, based on past movies and shows that you have liked. 

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Entertainment
- **Mobile:** WTW is built for easy simple and quick recommendations for users. This pairs well as a mobile application, as users can easily find recommendations on the go, with the device that is always with them. 
- **Story:** WhatToWatch helps with a world getting more and more bombarded with new shows and movies to watch. WTW helps people navigate this world and gives them the power to choose their recommendations by selecting what movies/shows the recommendations are based off of. This supercedes other recommenders (ie Netflix's recommendation list), that looks at everything you watched and gives a generic recommendation over all your watching medium. 
- **Market:** WhatToWatch is an app that can help anyone that watches shows and movies. 
- **Habit:** WhatToWatch can be used whenever someone wants to watch something new. Users can use it daily to help them find a nightly movie, or once a month to find a new show to binge. 
- **Scope:** The hardest part of the app will be understanding the ML component and using different API's to pull lists of movies and evaluate how similar movies are. 

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**
For App Functionality
* User must be able to build a list of movies by typing in name of movie
* App must provide unique recommendations based on the users list

From App Expectations
* App has multiple views
* Accounts and their respective lists and recommendations must be stored on a database
* User must be able to sign in to an account 
* User must be able to log in to an already existing account
* User can use the camera to upload a photo for their account
* User can use Facebook SDK to connect with friends in other accounts
* App uses some complex algorithm for determining movie recommendations
* User can pinch to adjust the number of items shown in list
* App uses animations for transitions
* App uses external library for improved UI

**Optional Nice-to-have Stories**

* Allow the user to create multiple lists to get different type of recommendations (ie a scary movie list, a comedy list, so that their recommendations are specific to the lists that they give)
* Have a search bar that helps autocomplete the user's title to titles of movies

### 2. Screen Archetypes

* MainActivity
   * Handles the management of multiple lists
   * Allows the user to add movies to a list
   * Shows recommendations per list
* MovieDetail
   * Shows specific movie details, such as the image, trailer, title, and description
* LoginActivity
   * Activity in charge of user login. Helps user login to their account
* AccountActivity
   * Shows the user their account
   * User can add a photo to their account
   * User can see all their lists

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* LoginActivity
* MainActivity
* MovieDetail

**Flow Navigation** (Screen to Screen)

* [list first screen here]
   * [list screen navigation here]
   * ...
* [list second screen here]
   * [list screen navigation here]
   * ...

## Wireframes
[Add picture of your hand sketched wireframes in this section]
![](https://i.imgur.com/RsMWg7F.jpg)


### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
