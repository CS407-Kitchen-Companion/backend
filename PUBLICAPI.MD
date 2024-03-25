# API Documentation (REST / CRUD)

## Notes

- Auth `Y?` means contend differs depending on login status

# User API (/user)

## Table of Contents

| Name        | Link                             | Description              |
|-------------|----------------------------------|--------------------------|
| User API    | [User API](#User-Public-API)     | User Related Functions   |
| Recipes API | [Recipe API](#Recipe-Public-API) | Recipe Related Functions |
| Ratings API | [Rating API](#Rating-Public-API) | Rating Related Functions |
| Folders API | [Folder API](#Folder-Public-API) | Rating Related Functions |

## API Endpoints

| Name          | Link                                                           | Description                                           |
|---------------|----------------------------------------------------------------|-------------------------------------------------------|
| Dev Endpoint  | `https://kitchencompanion.eastus.cloudapp.azure.com/api/v1/`   | Actual deployed/running SB Server                     |
| Mock Endpoint | `https://kitchencompanion.eastus.cloudapp.azure.com/api/mock/` | Python Django mock server (faster features, usually?) |

## User Public API

| Method | Mapping          | Description                        | Model/Body/Params                      | Auth | Status |
|--------|------------------|------------------------------------|----------------------------------------|------|--------|
| POST   | `/new`           | Used in new user Creation          | See [new user](#new-user)              | N    | Active |
| POST   | `/login`         | Used in user login                 | See [user login](#user-login)          | N    | Active |
| GET    | `/{id}`          | Get User Data                      | Params: uid, See [get user](#get-user) | N    | Active |
| GET    | `/{id}/username` | Get User Username Only             | Params: uid                            | N    | Active |
| GET    | `/saved`         | Get recipes saved by user          | See [user saved](#user-saved)          | Y    | Active |
| GET    | `/verify`        | Verify User email given token      | Params: uid, token                     | N    | Later  |
| GET    | `/all`           | Get information for all users      |                                        | N    | Later  |
| GET    | `/{id}/groups`   | Get list of groups created by user |                                        | N    | Later  |
| GET    | `/current`       | Get current user information       |                                        | Y?   | Later  | 
| DELETE | `/delete`        | Delete the current logged in user  |                                        | Y    | Later  | 

### User Info API (Other)

| Method | Mapping          | Description            | Model/Body/Params | Auth | Status |
|--------|------------------|------------------------|-------------------|------|--------|
| GET    | `/{id}/username` | Get User Username Only | Params: uid       | N    | Active |

### User Development API

| Method | Mapping     | Description                          | Model/Body/Params | Auth |
|--------|-------------|--------------------------------------|-------------------|------|
| GET    | `/testauth` | Development use check logged in user |                   | Y?   |

## User API Models

### new user

#### Request Body

```json
{
  "username": "username",
  "password": "password",
  "email": "email"
}
```

#### Response Body

```json
{
  "response": "Please Verify Email",
  "status": 200,
  "error": false,
  "data": {
    "id": 5,
    "username": "test_5",
    "email": "test5@email",
    "createdAt": "2024-02-22T22:32:28.053262Z"
  }
}
```

### user login

#### Request Body

```json
{
  "username": "test_2",
  "password": "test_2_pass"
}
```

#### Response Body

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

### get user

#### Request Body

N/A, use path: `/user/{id}`

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": {
    "id": 1,
    "username": "test_1",
    "email": "test@email",
    "createdAt": "2024-02-12T21:05:41.846385Z"
  }
}
```

### user saved

#### Request Body

N/A, use path: `/saved`

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": [
    2,
    3,
    4
  ]
}
```

## Recipe Public API

| Method | Mapping      | Description                 | Model/Body/Params                         | Auth | Status |
|--------|--------------|-----------------------------|-------------------------------------------|------|--------|
| POST   | `/new`       | Used in new Recipe Creation | See [new recipe](#new-recipe)             | Y    | Active |
| GET    | `/{id}`      | Used to get a Recipe        | See [get recipe](#get-recipe)             | N    | Active |
| GET    | `/get`       | Used to get several Recipes | See [get recipe multi](#get-recipe-multi) | N    | Active |
| GET    | `/{id}/save` | Used to get a Recipe        | Save a post as a user                     | Y    | Active |

## Recipe API Models

### new recipe

#### Request Body

Calories optional value

```json
{
  "title": "title 1",
  "content": ["direction1","direction2"],
  "serves": 5,
  "time": 3600,
  "calories": 105,
  "tags": [
    "abc",
    "def",
    "tag1"
  ],
  "appliances": [
    "Knife",
    "Litteral_Kitchen"
  ],
  "ingredients": [
    {
      "ingredient": "p1",
      "amount": 1.2,
      "unit": "kg"
    },
    {
      "ingredient": "p2",
      "amount": 137,
      "unit": "g"
    }
  ]
}
```

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": {
    "id": 1,
    "title": "title 1",
    "content": [
      "direction1",
      "direction2"
    ],
    "createdBy": 1,
    "createdAt": "2024-03-25T22:45:18.900713395Z",
    "comments": [],
    "ratings": [],
    "appliances": [
      "Knife",
      "Litteral_Kitchen"
    ],
    "tags": [
      "abc",
      "def",
      "tag1"
    ],
    "ingredients": [
      {
        "id": 1,
        "name": "p1",
        "amount": 1.2,
        "unit": "kg"
      },
      {
        "id": 2,
        "name": "p2",
        "amount": 137.0,
        "unit": "g"
      }
    ],
    "ratingCount": 0,
    "calculatedRating": 0,
    "serves": 5,
    "time": 3600,
    "calories": 105,
    "updatedAt": "2024-03-25T22:45:18.900713395Z",
    "edited": false
  }
}
```

### get recipe

#### Request Body

N/A, use path: `/recipe/{id}`

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": {
    "id": 24,
    "title": "title 1",
    "content": [
      "direction1",
      "direction2"
    ],
    "createdBy": 2,
    "createdAt": "2024-02-20T23:43:38.226555Z",
    "comments": [],
    "ratings": [
      2
    ],
    "appliances": [
      "Knife",
      "Literal_Kitchen"
    ],
    "tags": [
      "abc",
      "def",
      "tag1"
    ],
    "ingredients": {
      "i1": "p1",
      "i2": "p2"
    },
    "ratingCount": 1,
    "calculatedRating": 5,
    "serves": 5,
    "time": 3600,
    "calories": 105,
    "updatedAt": "2024-02-20T23:44:21.954814Z",
    "edited": false
  }
}
```

### get recipe multi

#### Request Body

```json
{
  "recipes": [
    1,
    2,
    3
  ]
}
```

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": [
    ...
  ]
}
```

## Rating Public API

| Method | Mapping | Description                 | Model/Body/Params             | Auth | Status |
|--------|---------|-----------------------------|-------------------------------|------|--------|
| POST   | `/new`  | Used in new Rating Creation | See [new rating](#new-rating) | Y    | Active |
| GET    | `/{id}` | Used to get a Rating        | See [get rating](#get-rating) | N    | Active |

## Rating API Models

### new rating

#### Request Body

```json
{
  "recipe_id": 1,
  "rating": 5
}
```
:wq
#### Response Body

```json
{
  "response": "See Data.",
  "status": 201,
  "error": false,
  "data": {
    "id": 1,
    "recipe": 1,
    "createdBy": 2,
    "rating": 5
  }
}
```

### get rating

#### Request Body

N/A, use path: `/rating/{id}`

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": {
    "id": 1,
    "recipe": 1,
    "createdBy": 2,
    "rating": 5
  }
}
```

## Search Public API

| Method | Mapping                      | Description                 | Model/Body/Params             | Auth | Status |
|--------|------------------------------|-----------------------------|-------------------------------|------|--------|
| GET    | `/recipe/search?tags=&tags=` | Used to search for a recipe | See [get search](#get-search) | N    | Active |

### get search

#### Request Body

N/A, use path: `/recipe/search?tags=abc`

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": [
    {
      "id": 3,
      "title": "title 1",
      "content": [],
      "createdBy": 2,
      "createdAt": "2024-02-20T21:18:58.875501Z",
      "comments": [],
      "ratings": [],
      "appliances": [
        "Knife",
        "Litteral_Kitchen"
      ],
      "tags": [
        "abc",
        "def",
        "tag1"
      ],
      "ingredients": {
        "i1": "p1",
        "i2": "p2"
      },
      "ratingCount": null,
      "calculatedRating": null,
      "serves": 5,
      "time": 3600,
      "calories": null,
      "updatedAt": "2024-02-20T21:18:58.875501Z",
      "edited": false
    }
  ]
}
```

## Folder Public API

| Method | Mapping | Description                 | Model/Body/Params                     | Auth | Status |
|--------|---------|-----------------------------|---------------------------------------|------|--------|
| POST   | `/new`  | Used in new Folder Creation | See [new folder](#new-folder)         | Y    | Active |
| GET    | `/{id}` | Used to get a Folder        | See [get folder](#get-folder)         | N*   | Active |
| POST   | `/save` | Save a recipe to the folder | See [save to folder](#save-to-folder) | Y    | Active |

### new folder

#### Request Body

```json
{
  "title": "test folder 1"
}
```

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": {
    "id": 1,
    "recipies": [],
    "title": "test folder 1",
    "createdBy": 4
  }
}
```

### get folder

#### Request Body

N/A Use Path `/folder/{id}`

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": {
    "id": 1,
    "recipies": [
      3
    ],
    "title": "test folder 1",
    "createdBy": 4
  }
}
```

### save to folder

#### Request Body

```json
{
  "folder": 1,
  "recipe": 3
}
```

#### Response Body

```json
{
  "response": "See Data.",
  "status": 200,
  "error": false,
  "data": {
    "id": 1,
    "recipies": [
      3
    ],
    "title": "test folder 1",
    "createdBy": 4
  }
}
```