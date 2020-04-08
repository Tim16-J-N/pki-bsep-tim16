// This file can be replaced during build by using the `fileReplacements` array.
// `ng build --prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.

export const environment = {
  production: false,
  itemsPerPage: 5,
  baseUrl: 'http://localhost:8080',
  entity: '/api/entity',
  ocsp: '/api/ocsp',
  certificate: '/api/certificate',
  auth: '/api/auth',
};
