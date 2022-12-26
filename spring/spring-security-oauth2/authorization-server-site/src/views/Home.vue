<script setup lang="ts">
  import SpringAuthorizationServer from '../components/SpringAuthorizationServer.vue';
</script>

<template>
  <div class="home">
    <div class="box">
      <div>
        <a href="https://oauth.net/2/" target="_blank">
          <img src="/oauth-2-sm.png" class="logo oauth2" alt="OAuth2.0" />
        </a>
        <a href="https://openid.net/developers/specs/" target="_blank">
          <img src="/oidc.jpeg" class="logo oidc" alt="OIDC" />
        </a>
        <a href="https://spring.io/projects/spring-authorization-server" target="_blank">
          <img
            src="/Spring-Security-logo.png"
            class="logo spring"
            alt="Spring Authorization Server"
          />
        </a>
      </div>
      <SpringAuthorizationServer msg="Spring Authorization Server" />
    </div>
  </div>
</template>

<style scoped>
  .home {
    max-width: 1280px;
    margin: 0 auto;
    text-align: center;
    min-width: 320px;
    min-height: 100vh;

    display: flex;
    place-items: center;
  }

  .box {
    max-width: 1280px;
    margin: 0 auto;
    text-align: center;
    min-width: 320px;
  }

  a {
    font-weight: 500;
    color: #646cff;
    text-decoration: inherit;
  }

  a:hover {
    color: #535bf2;
  }

  .logo {
    height: 6em;
    padding: 1.5em;
    will-change: filter;
  }

  .logo.oauth2:hover {
    filter: drop-shadow(0 0 2em #646cffaa);
  }

  .logo.oidc:hover {
    filter: drop-shadow(0 0 2em #ab6024);
  }

  .logo.spring:hover {
    filter: drop-shadow(0 0 2em #42b883aa);
  }
</style>
