# Individual_Refrig

냉장고를 부탁해 ver2

# Introduction
This application is for managing the ingredients in the user refrigerator and recommending the recipes that they can make using those ingredients.

This application was originally a web application but I thought it would be easier to use this application on mobile devices. 
This is why I made version 2 of this project. 

This application is currently only for Android OS.
If the user wants to use the barcode reading function,the user has to give their camera permissions.

# Function
0. User can signup/signin
1. User can add ingredients in the database by reading barcode using their phone camera or type in the ingredients themselves.
1-1. If application doesn't have barcode data, user can add barcode data to database.
     ( For data stability, it needs to be changed.)
2. User can see the ingredients they have in the main page.
3. User can get see the recipes they can make.
3-1. User can choose option to see the recipes. 
     (options : 0 more ingredients needed recipes, 1 more ingredients needed recipes, 2 more ingredients needed recipes, 3+ more ingredients needed recipes)

# Design
Design is simple and mostly undecorated.

## 
Using Android Studio (JAVA), Firebase(cloud storage, authorization), Google API (Barcode API)


