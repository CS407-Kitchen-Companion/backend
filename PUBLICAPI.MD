# API Documentation (REST / CRUD)

## Notes

- Auth `Y?` means contend differs depending on login status

# User API (/user)

## Table of Contents

| Name         | Link                        | Description               |
|--------------|-----------------------------|---------------------------|
| User API     | [here](#User-Public-API)    | User Related Functions    |
| Recipies API | [here](#Recipie-Public-API) | Recipie Related Functions |

## API Endpoints

| Name          | Link                                                           | Description                                           |
|---------------|----------------------------------------------------------------|-------------------------------------------------------|
| Dev Endpoint  | `https://kitchencompanion.eastus.cloudapp.azure.com/api/v1/`   | Actual delpoyed/running SB Server                     |
| Mock Endpoint | `https://kitchencompanion.eastus.cloudapp.azure.com/api/mock/` | Python Django mock server (faster features, usually?) |

## User Public API

| Method | Mapping        | Description                        | Model/Body/Params             | Auth | Status |
|--------|----------------|------------------------------------|-------------------------------|------|--------|
| POST   | `/new`         | Used in new user Creation          | See [new user](#new-user)     | N    | Active |
| POST   | `/login`       | Used in new user Creation          | See [user login](#user-login) | N    | Active |
| GET    | `/verify`      | Verify User email given token      | Params: uid, token            | N    | Later  |
| GET    | `/all`         | Get information for all users      |                               | N    | Later  |
| GET    | `/{id}/boards` | Get list of groups created by user |                               | N    | Later  |
| GET    | `/current`     | Get current user information       |                               | Y?   | Later  | 
| DELETE | `/delete`      | Delete the current logged in user  |                               | Y    | Later  | 

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

#### Reply Body

```json
{
  "response": "Please Verify Email",
  "status": 200,
  "error": false
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

## Recipie Public API

| Method | Mapping | Description               | Model/Body/Params               | Auth | Status |
|--------|---------|---------------------------|---------------------------------|------|--------|
| POST   | `/new`  | Used in new user Creation | See [new recipie](#new-recipie) | N    | Active |

# Anything further is not valid

## Discussion API (/discussions)

### Discussion Public API

| Method | Mapping        | Description                                          | Model/Body/Params    | Auth | Status |
|--------|----------------|------------------------------------------------------|----------------------|------|--------|
| POST   | `/new`         | Used in new discussion creation                      | See `DiscussionNew`  | Y    | Active |
| PUT    | `/{id}`        | Edit discussion information                          | See `DiscussionEdit` | Y    | Active | 
| GET    | `/all`         | Get list of all discussions                          |                      | N    | Active |
| GET    | `/{id}`        | Get information for specified discussion             |                      | N    | DEPREC | 
| GET    | `/get`         | Get information for one or more discussions          |                      | N    | DEV-A  |
| GET    | `/{id}/posts`  | Get list of all posts created in discussion          |                      | N    | DEPREC |
| GET    | `/{id}/posts2` | Get list of all posts created in discussion (faster) |                      | N    | DEV-A  |
| DELETE | `/{id}`        | Delete the given discussion                          |                      | Y    | Active |

### Discussion API Models

#### DiscussionNew

```json
{
  "title": "title",
  "description": "description"
}
```

#### DiscussionEdit

```json
{
  "title": "title",
  "description": "description"
}
```

#### DiscussionGet(V2)

URL param

| Variable | Optional | Description                        |
|----------|----------|------------------------------------|
| list     | No       | Query Param, used for many at once |

Example usage:
`http://localhost:8080/discussion/get?list=23,24`

### Post Public API

| Method | Mapping          | Description                               | Model/Body/Params    | Auth | Status   |
|--------|------------------|-------------------------------------------|----------------------|------|----------|
| POST   | `/new`           | Used in new post creation                 | See `DiscussionNew`  | Y    | Active   |
| PUT    | `/{id}`          | Edit post information                     | See `DiscussionEdit` | Y    | Active   | 
| GET    | `/all`           | Get list of all post                      |                      | N    | Active   |
| GET    | `/{id}`          | Get information for specified post        |                      | N    | DEPREC   | 
| GET    | `/get`           | Get information for one or more posts     |                      | N    | DEV-A    |
| GET    | `/{id}/comments` | Get list of all comments created in posts |                      | N    | NEWDEV-B |
| DELETE | `/{id}`          | Delete the given post                     |                      | Y    | Active   |

### Discussion API Models