# API Documentation (REST / CRUD)

## Notes

- Note: `Y?` Refers to functionally not strictly required, but is meant to be used.
- Does not show all data saved, or possible accessible, as they are private to the API
- Only shows that which is accessible via the API

## User API (/user)

### User Public API

| Method | Mapping        | Description                        | Model/Body/Params  | Auth | Status |
|--------|----------------|------------------------------------|--------------------|------|--------|
| POST   | `/new`         | Used in new user Creation          | See `UserNew`      | N    | Active |
| PUT    | `/update`      | Edit user information              | See `UserUpdate`   | Y    | Active |
| GET    | `/verify`      | Verify User email given token      | Params: uid, token | N    | Active |
| GET    | `/all`         | Get information for all users      |                    | N    | Active |
| GET    | `/{id}/boards` | Get list of boards created by user |                    | N    | Active |
| GET    | `/current`     | Get current user information       |                    | Y?   | Active | 
| DELETE | `/delete`      | Delete the current logged in user  | See `UserDelete`   | Y    | Active | 

### User Development API

| Method | Mapping     | Description                          | Model/Body/Params | Auth |
|--------|-------------|--------------------------------------|-------------------|------|
| GET    | `/testauth` | Development use check logged in user |                   | Y?   |

### User API Models

#### UserNew

```JSON
{
  "username": "username",
  "password": "password",
  "email": "email"
}
```

#### UserUpdate

```JSON
{
  "oldpassword": "oldpassword",
  "newpassword": "newpassword",
  "newusername": "username",
  "newemail": "email"
}
```

#### UserDelete

```JSON
{
  "password": "password"
}
```

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

| Method | Mapping           | Description                                        | Model/Body/Params    | Auth | Status   |
|--------|-------------------|----------------------------------------------------|----------------------|------|----------|
| POST   | `/new`            | Used in new post creation                          | See `DiscussionNew`  | Y    | Active   |
| PUT    | `/{id}`           | Edit post information                              | See `DiscussionEdit` | Y    | Active   | 
| GET    | `/all`            | Get list of all post                               |                      | N    | Active   |
| GET    | `/{id}`           | Get information for specified post                 |                      | N    | DEPREC   | 
| GET    | `/get`            | Get information for one or more posts              |                      | N    | DEV-A    |
| GET    | `/{id}/comments`  | Get list of all comments created in posts          |                      | N    | NEWDEV-B |
| DELETE | `/{id}`           | Delete the given post                              |                      | Y    | Active   |

### Discussion API Models