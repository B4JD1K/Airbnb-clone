// kod wykorzystywany w Auth0 Actions, umożliwia dostosowanie login-flow w systemie uwierzytelniania i autoryzacji Auth0

/**
* Handler that will be called during the execution of a PostLogin flow.
*
* @param {Event} event - Details about the user and the context in which they are logging in.
* @param {PostLoginAPI} api - Interface whose methods can be used to change the behavior of the login.
*/

// handler wywoływany w trakcie przepływu PostLogin (po udanym logowaniu)
// - event: zawiera szczegóły dotyczące użytkownika i kontekstu logowania
//   - dane usera: event.user (np. user_id, email, metadata)
//   - role usera: event.authorization.roles (jeśli przypisane w Auth0 Dashboard)
//   - kontekst logowania: event.transaction (np. protokół logowania - OAuth2/SAML, język, metoda logowania)
// - api: interfejs do modyfikacji procesu logowania
//   - umożliwia np. dodawanie niestandardowych claims do tokenów
exports.onExecutePostLogin = async (event, api) => {
  const namespace = "https://www.b4jd1k.pl"

  // jeśli user ma przypisane role w `event.authorization`, dodaj je do tokenów
  if (event.authorization && event.authorization.roles && event.authorization.roles.length > 0){
    if(event.authorization){
      api.idToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles); // dodaj role do id token
      api.accessToken.setCustomClaim(`${namespace}/roles`, event.authorization.roles); // dodaj role do access token
    }
    return // zakończenie działania, bo role zostały dodane
  }

  // jeśli użytkownik nie ma ról, przypisz rolę za pomocą Management API i zaktualizuj tokeny
  const ManagementClient = require('auth0').ManagementClient;

  // dane uwierzytelniające dla Management API
  const management = new ManagementClient({
    domain: event.secrets.DOMAIN,
    clientId: event.secrets.CLIENT_ID,
    clientSecret: event.secrets.CLIENT_SECRET,
  })

  const params = {id: event.user.user_id}; // id użytkownika
  const data = {"roles":["rol_LiAXp9UWq9nYeXa3"]} // rola do przypisania

  // pozwalają na dodanie niestandardowych claims (np. role użytkownika) do tokenów ID i Access
  try{
    await management.users.assignRoles(params, data); // przypisanie roli użytkownikowi
    const rolesData = await management. users.getRoles(params); // pobranie ról użytkownika
    const rolesName = rolesData.data.map(role => role.name); // mapowanie nazwy ról
      api.idToken.setCustomClaim(`${namespace}/roles`, rolesName); // ustawienie claims w tokenach (id)
      api.accessToken.setCustomClaim(`${namespace}/roles`, rolesName); // - || - (Access)
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
*/
// exports.onContinuePostLogin = async (event, api) => {
// };
