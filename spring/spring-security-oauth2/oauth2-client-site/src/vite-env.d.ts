/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly VITE_APP_SSO_LOGIN_URL: string
  readonly VITE_APP_BASE_URL: string
}

interface ImportMeta {
  readonly env: ImportMetaEnv
}