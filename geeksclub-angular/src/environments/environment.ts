export const environment = {
  production: false,
  apiBaseUrl: 'http://localhost:8080'
};

export const getApiBaseUrl = (): string => {
  return (window as any)['env']['apiUrl'];
}
