---
sidebar_position: 11
---
# Provider Permissions

Within Accuro, Provider Permissions give users access to specified types of data for certain providers. For example, an Accuro user may be given access to `Labs` and `Scheduling` data for **Provider X**, only `Scheduling` data for **Provider Y**, and no access to **Provider Z**.



As you can see above in the User Management screen in Accuro, users have specific permissions for each office and for each provider in the office. **Provider X** exists in two offices and the user has different permissions for that provider for each office, allowing for granular control over the data associated with providers.

## Overview of Provider Permissions

Provider Permissions specify the type of data for each Provider a user has access to. When setting Provider Permissions for a user, they can be given access to the following categories:

- **Patient** - Able to change a patient's provider
- **Scheduling** - View and modify appointments for the provider
- **Billing** - View and modify billing data for the provider
- **Inbox** - View a provider's inbox, e.g. see which labs or documents are in a provider's inbox for review
- **EMR** - Enables a provider to be selected from various dropdown lists
- **Labs** - View and modify lab tests and results for the provider
- **Reporting** - Includes the provider in reports
- **Visible** - Includes the provider in selection dropdowns etc.
- **Prescribe For** - View and modify prescriptions by the provider
- **Access Until** - The preceding permissions are only in effect and give access to the provider until the set date

Some permissions can have either Full Access (able to read and write data) and Read-Only Access (able to read data only).

Currently, the provider permissions enforced by Accuro API are the **Patient**, **Scheduling**, **Billing**, **EMR**, and **Labs**.

## Relating Provider Permissions with Accuro API's **Service User**

If your application is using  provider permissions set for the **Service User** used by your application will impact what data your application will be able to access.

## How Accuro API enforces Provider Permissions

Depending on the type of resource being accessed, e.g. a collection of resources, individual resources or supporting information resources not necessarily tied to specific providers (e.g. lab templates), Accuro API will enforce provider permissions differently. Below are the possible ways Accuro API will enforce provider permissions.

### Filtered results

When requesting a collection of resources Accuro API will only return resources that the user has permission to view and will filter out results that the user does not have permissions to access. If the user does not have any permissions to any providers, the API will return an empty array.

For example, retrieving a list of appointments will only return results that have an appointment provider that the **Service User** has the `Scheduling` provider permission for.

### 403 Forbidden - Insufficient Permissions Error

When requesting a resource or a collection of resources that, in order to access, requires the **Service User** to have a specific permission for at least one provider Accuro API will return a `403 Forbidden` error with a `Insufficient Permissions` message. The resources that will return this error are likely supporting information resources.

For example, retrieving the list of available types of lab tests that exist in Accuro, the **Service User** requires the `Labs` permission for at least one provider. If the user does not have the `Labs` permission for at least one provider, Accuro API will return a `403 Forbidden` with an `Insufficient Permissions` message.

Or, for example updating an appointment reason associated with a provider and an office where the **Service User** does not have the `Scheduling` permission for the specified provider in the specified office, Accuro API will return a `403 Forbidden` with an `Insufficient Permissions` message.

### Using the API Reference

The Accuro API Reference documentation has more information on what provider permissions are needed and for which providers associated with the resource for each endpoint to access.



The Accuro API reference has the type of provider permission required, the level of access (i.e. read/write) and a description of what data the user has access to when they have the required provider permissions. In the example above, when the user has the `Scheduling` provider permission for a provider, appointments with that provider will be returned (along with any appointments with a provider that the user also has the required provider permission for).


