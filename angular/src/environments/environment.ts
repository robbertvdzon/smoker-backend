// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `.angular-cli.json`.

export const environment = {
  production: false,
  getStatusUrl: 'assets/getstatus_loggedin.json',
  // getStatusUrl: 'assets/getstatus.json',
  getAddRequiredTempUrl: 'assets/getaddreqtemp.json',
  putSettings: 'api/setSettings',
  clearDataUrl: 'api/clearData',
  getAllUrl: 'assets/getall.json',
  getLastUrl: 'assets/getlast.json',

  KEYCLOAK_URL: 'http://localhost:8090/auth',
  KEYCLOAK_REALM: 'smoker',
  KEYCLOAK_CLIENTID: 'smoker-frontend',
  BACKEND_URL: 'http://localhost:8080/api'
};
