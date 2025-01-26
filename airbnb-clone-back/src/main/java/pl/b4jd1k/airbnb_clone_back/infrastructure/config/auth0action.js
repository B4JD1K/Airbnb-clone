/**
 * Handler that will be called during the execution of a PostLogin flow.
 *
 * @param {Event} event - Details about the user and the context in which they are logging in.
 * @param {PostLoginAPI} api - Interface whose methods can be used to change the behavior of the login.
 */

exports.onExecutePostLogin = async (event, api) => {
  const namespace = "https://www.b4jd1k.pl"

  if (event.authorization && event.authorization.roles && event.authorization.roles.length > 0) {
    if (event.authorization) {
      api.idToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles);
      api.accessToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles);
    }
    return;
  }

  const ManagementClient = require('auth0').ManagementClient;

  const management = new ManagementClient({
    domain: event.secrets.DOMAIN,
    clientId: event.secrets.CLIENT_ID,
    clientSecret: event.secrets.CLIENT_SECRET,
  })

  const params = {id: event.user.user_id};
  const data = {"roles": ["rol_LiAXp9UWq9nYeXa3"]}

  try {
    await management.users.assignRoles(params, data);
    const rolesData = await management.users.getRoles(params);
    const rolesName = rolesData.data.map(role => role.name);
    api.idToken.setCustomClaim(`${namespace}/roles`, rolesName);
    api.accessToken.setCustomClaim(`${namespace}/roles`, rolesName);
  } catch (e) {
    console.log(e);
  }

};
/**
 * Handler that will be invoked when this action is resuming after an external redirect. If your
 * onExecutePostLogin function does not perform a redirect, this function can be safely ignored.
 *
 * @param {Event} event - Details about the user and the context in which they are logging in.
 * @param {PostLoginAPI} api - Interface whose methods can be used to change the behavior of the login.
 *
 * TEMPLATE:
 * exports.onContinuePostLogin = async (event, api) => {
 * };
 */

