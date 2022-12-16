<script setup lang="ts">
import HelloWorld from '../components/SpringAuthorizationServer.vue';</script>

<template>
  <div>
    <a href="https://oauth.net/2/" target="_blank">
      <img src="/oauth-2-sm.png" class="logo" alt="Spring Authorization Server"/>
    </a>
    <a href="https://openid.net/developers/specs/" target="_blank">
      <img src="/oidc.jpeg" class="logo vue" alt="OIDC"/>
    </a>
    <a href="https://spring.io/projects/spring-authorization-server" target="_blank">
      <img src="/Spring-Security-logo.png" class="logo vue" alt="Spring Authorization Server"/>
    </a>
  </div>
  <HelloWorld msg="Spring Authorization Server"/>
</template>

<style scoped>
.logo {
  height: 6em;
  padding: 1.5em;
  will-change: filter;
}

.logo:hover {
  filter: drop-shadow(0 0 2em #646cffaa);
}

.logo.vue:hover {
  filter: drop-shadow(0 0 2em #42b883aa);
}
</style>
