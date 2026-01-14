# Keycloak Setup Guide for Tickety

This guide provides detailed instructions for setting up Keycloak for the Tickety event ticket platform, including
configuration for development, testing, and production environments.

## 🎯 Overview

Tickety uses Keycloak as its authentication and authorization server. The setup includes:

- **Realm**: `tickety` - The security realm for the application
- **Client**: `tickety-client` - The client application
- **Roles**: `ORGANIZER`, `ATTENDEE`, `STAFF` - Application roles
- **Users**: Sample users for testing different roles

## 🚀 Quick Start with Docker

The project includes a Docker Compose configuration that starts Keycloak automatically:

```bash
docker compose up -d
```

This will:

- Start Keycloak on port 9090
- Use admin/admin as administrator credentials
- Create a development setup with H2 database

Access Keycloak Admin Console: `http://localhost:9090`

## 🔧 Detailed Keycloak Setup

### 1. Access Keycloak Admin Console

1. Open your browser and navigate to: `http://localhost:9090`
2. Log in with:
    - Username: `admin`
    - Password: `admin`
3. You'll be prompted to create an initial admin password (keep it as `admin` for development)

### 2. Create Tickety Realm

1. Hover over the "Master" realm dropdown in the top-left corner
2. Click "Add realm"
3. Enter:
    - **Name**: `tickety`
    - **Enabled**: ON
4. Click "Create"

### 3. Create Client Application

1. In the `tickety` realm, go to "Clients" in the left menu
2. Click "Create client"
3. Configure the client:
    - **Client ID**: `tickety-client`
    - **Client type**: `OpenID Connect`
    - **Client authentication**: `On`
4. Click "Next"

5. **Capability config**:
    - **Client authentication**: `On`
    - **Authorization**: `Off`
    - **Standard flow**: `On`
    - **Direct access grants**: `On` (for testing with username/password)
6. Click "Next"

7. **Login settings**:
    - **Root URL**: `http://localhost:8080`
    - **Valid redirect URIs**: `http://localhost:8080/*`
    - **Web origins**: `http://localhost:8080`
8. Click "Save"

9. Go to the "Credentials" tab of your client:
    - Note the **Client secret** (you may need it for some configurations)
    - Keep **Client authentication**: `On`

### 4. Create Roles

1. Go to "Realm roles" in the left menu
2. Click "Create role" for each of the following:

**ORGANIZER Role:**

- **Role name**: `ORGANIZER`
- **Description**: "Can create and manage events"
- Click "Save"

**ATTENDEE Role:**

- **Role name**: `ATTENDEE`
- **Description**: "Can browse events and purchase tickets"
- Click "Save"

**STAFF Role:**

- **Role name**: `STAFF`
- **Description**: "Can validate tickets at events"
- Click "Save"

### 5. Create Test Users

#### Organizer User

1. Go to "Users" in the left menu
2. Click "Add user"
3. Enter:
    - **Username**: `organizer`
    - **Email**: `organizer@example.com`
    - **First Name**: `Event`
    - **Last Name**: `Organizer`
4. Click "Create"
5. Go to the "Credentials" tab:
    - Set password: `password`
    - **Temporary**: `Off` (so user doesn't need to change password)
6. Click "Set Password"
7. Go to the "Role mapping" tab:
    - Click "Assign role"
    - Select `ORGANIZER` from the "Realm Roles" list
    - Click "Assign"

#### Attendee User

1. Click "Add user"
2. Enter:
    - **Username**: `attendee`
    - **Email**: `attendee@example.com`
    - **First Name**: `Ticket`
    - **Last Name**: `Attendee`
3. Click "Create"
4. Go to the "Credentials" tab:
    - Set password: `password`
    - **Temporary**: `Off`
5. Click "Set Password"
6. Go to the "Role mapping" tab:
    - Click "Assign role"
    - Select `ATTENDEE` from the "Realm Roles" list
    - Click "Assign"

#### Staff User

1. Click "Add user"
2. Enter:
    - **Username**: `staff`
    - **Email**: `staff@example.com`
    - **First Name**: `Event`
    - **Last Name**: `Staff`
3. Click "Create"
4. Go to the "Credentials" tab:
    - Set password: `password`
    - **Temporary**: `Off`
5. Click "Set Password"
6. Go to the "Role mapping" tab:
    - Click "Assign role"
    - Select `STAFF` from the "Realm Roles" list
    - Click "Assign"

#### Admin User (Optional)

For users who need multiple roles:

1. Click "Add user"
2. Enter:
    - **Username**: `admin`
    - **Email**: `admin@example.com`
    - **First Name**: `System`
    - **Last Name**: `Admin`
3. Click "Create"
4. Set password: `password` (Temporary: Off)
5. Assign all three roles: `ORGANIZER`, `ATTENDEE`, `STAFF`

### 6. Configure Token Settings (Optional)

For production or specific testing needs:

1. Go to "Realm settings" > "Tokens"
2. Adjust token lifespans as needed:
    - **Access Token Lifespan**: 300 seconds (5 minutes) for development
    - **Client Session Idle**: 1800 seconds (30 minutes)
    - **Client Session Max**: 3600 seconds (1 hour)

### 7. Configure Client Scopes (Optional)

For enhanced security:

1. Go to "Client scopes"
2. Create a new scope called `tickety-roles`
3. Add a mapper:
    - **Name**: `realm roles`
    - **Mapper Type**: `User Realm Role`
    - **Token Claim Name**: `realm_access.roles`
    - **Add to ID token**: `On`
    - **Add to access token**: `On`
    - **Add to userinfo**: `On`

## 🔐 Getting Access Tokens

### Method 1: Using Keycloak Admin Console

1. Log in to Keycloak Admin Console
2. Go to your realm
3. Click on "Users" in the left menu
4. Select a user (e.g., `organizer`)
5. Go to the "Credentials" tab
6. Use the "Get access token" feature

### Method 2: Using cURL

```bash
# Get token for organizer
curl -X POST "http://localhost:9090/realms/tickety/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=tickety-client" \
  -d "username=organizer" \
  -d "password=password" \
  -d "grant_type=password"

# Get token for attendee
curl -X POST "http://localhost:9090/realms/tickety/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=tickety-client" \
  -d "username=attendee" \
  -d "password=password" \
  -d "grant_type=password"

# Get token for staff
curl -X POST "http://localhost:9090/realms/tickety/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=tickety-client" \
  -d "username=staff" \
  -d "password=password" \
  -d "grant_type=password"
```

### Method 3: Using Postman

1. Create a new POST request to: `http://localhost:9090/realms/tickety/protocol/openid-connect/token`
2. Set headers:
    - `Content-Type: application/x-www-form-urlencoded`
3. Add form-data parameters:
    - `client_id`: `tickety-client`
    - `username`: `your-username`
    - `password`: `your-password`
    - `grant_type`: `password`
4. Send the request and copy the `access_token` from the response

### Method 4: Using Keycloak JavaScript Adapter (for frontend)

```javascript
const keycloak = Keycloak({
  url: 'http://localhost:9090',
  realm: 'tickety',
  clientId: 'tickety-client'
});

keycloak.init({ onLoad: 'login-required' }).then(authenticated => {
  if (authenticated) {
    console.log('Access Token:', keycloak.token);
    console.log('Refresh Token:', keycloak.refreshToken);
  }
});
```

## 🧪 Testing Authentication

### Test API Access with Different Roles

```bash
# Test as organizer (should succeed)
curl -X GET "http://localhost:8080/api/v1/events" \
  -H "Authorization: Bearer ORGANIZER_TOKEN"

# Test as attendee (should fail - 403 Forbidden)
curl -X GET "http://localhost:8080/api/v1/events" \
  -H "Authorization: Bearer ATTENDEE_TOKEN"

# Test public endpoint (should succeed without token)
curl -X GET "http://localhost:8080/api/v1/published-events"

# Test attendee endpoint (should succeed)
curl -X GET "http://localhost:8080/api/v1/tickets" \
  -H "Authorization: Bearer ATTENDEE_TOKEN"
```

### Verify Token Contents

You can decode the JWT token to verify its contents:

```bash
# Using jwt.io or similar tools
# Or using command line:
echo "YOUR_ACCESS_TOKEN" | cut -d '.' -f2 | base64 -d | jq .
```

The token should contain:

- `sub`: User ID
- `preferred_username`: Username
- `realm_access.roles`: Array containing the user's roles

## 🔧 Troubleshooting

### Common Issues and Solutions

#### Issue: "Invalid token" or "Unauthorized" errors

- Solution: Ensure your Keycloak server is running
- Verify the issuer URI matches: `http://localhost:9090/realms/tickety`
- Check that the token hasn't expired
- Verify the token contains the required roles

#### Issue: Roles not being recognized

- Solution: Check that roles are properly assigned to users
- Verify the `jwtGrantedAuthoritiesConverter` in `SecurityConfig.java`
- Ensure the token contains `realm_access.roles`

#### Issue: Cannot access Keycloak admin console

- Solution: Verify Docker containers are running: `docker ps`
- Check ports: Keycloak should be on port 9090
- Try restarting: `docker compose restart keycloak`

#### Issue: Database connection issues

- Solution: Ensure PostgreSQL container is running
- Verify database credentials in `application.yaml`
- Check network connectivity between containers

## 🛡️ Security Best Practices

### For Development

1. **Use strong passwords** even in development
2. **Regularly reset** test user passwords
3. **Clean up** test data periodically
4. **Monitor** Keycloak logs for suspicious activity

### For Production

1. **Change all default credentials** (admin password, database passwords)
2. **Use HTTPS** for all Keycloak communications
3. **Configure proper token lifespans** (shorter for production)
4. **Set up monitoring and alerts** for authentication events
5. **Use a production database** (PostgreSQL, MySQL) instead of H2
6. **Configure CORS** properly for your production domains
7. **Set up rate limiting** to prevent brute force attacks
8. **Regularly update** Keycloak to the latest secure version

## 📋 Production Configuration

For production deployment, modify the `compose.yaml`:

```yaml
keycloak:
  image: quay.io/keycloak/keycloak:26.4
  command:
    - start
    - --db=postgres
    - --db-url=jdbc:postgresql://postgres:5432/keycloak
    - --db-username=keycloak
    - --db-password=secure-password
    - --proxy=edge
    - --hostname=auth.yourdomain.com
  environment:
    - KEYCLOAK_ADMIN=admin
    - KEYCLOAK_ADMIN_PASSWORD=very-secure-password
    - KC_DB=postgres
    - KC_PROXY=edge
    - KC_HOSTNAME=auth.yourdomain.com
```

And update your `application.yaml`:

```yaml
spring:
  security:
    oauth2:
      resourceserver:
        jwt:
          issuer-uri: https://auth.yourdomain.com/realms/tickety
          jwk-set-uri: https://auth.yourdomain.com/realms/tickety/protocol/openid-connect/certs
```

## 🔄 Token Refresh

To refresh an expired token:

```bash
curl -X POST "http://localhost:9090/realms/tickety/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "client_id=tickety-client" \
  -d "grant_type=refresh_token" \
  -d "refresh_token=YOUR_REFRESH_TOKEN"
```

## 📚 Additional Resources

- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [Spring Security OAuth2 Resource Server](https://docs.spring.io/spring-security/reference/servlet/oauth2/resource-server/index.html)
- [JWT.io Debugger](https://jwt.io) - For decoding and verifying tokens

## 🤝 Support

For issues with Keycloak setup, you can:

1. Check the [Keycloak community forums](https://keycloak.discourse.group/)
2. Review the [Keycloak GitHub issues](https://github.com/keycloak/keycloak/issues)
3. Consult the [Spring Security documentation](https://spring.io/projects/spring-security)

This guide should provide everything you need to set up and test Keycloak authentication for the Tickety platform. Happy
coding! 🚀
