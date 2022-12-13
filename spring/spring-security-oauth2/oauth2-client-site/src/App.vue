<script setup lang="ts">
import SpringSecurityOAuth2Client from './components/SpringSecurityOAuth2Client.vue'</script>

<template>
  <div>
    <a href="https://oauth.net/2/" target="_blank">
      <img src="/oauth-2-sm.png" class="logo" alt="OAuth2 logo"/>
    </a>
    <a href="https://spring.io/projects/spring-security" target="_blank">
      <img src="/Spring-Security-logo.png" class="logo vue" alt="Spring Security logo"/>
    </a>
  </div>
  <SpringSecurityOAuth2Client msg="Spring Security OAuth2 Client"/>
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
