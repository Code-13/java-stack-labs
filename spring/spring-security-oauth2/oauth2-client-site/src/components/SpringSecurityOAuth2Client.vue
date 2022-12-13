<script setup lang="ts">
import {ref} from "vue";
import request from "../utils/request";

const hello = ref();

request.get("/").then(value => {
  hello.value = value.data
});

defineProps<{ msg: string }>()

</script>

<template>
  <h1>{{ msg }}</h1>

  <div class="card">
  </div>

  <p>
    {{ hello }}
  </p>
  <p>

  </p>
  <p class="read-the-docs">Click on the Spring Security and OAuth2 logos to learn more</p>
</template>

<style scoped>
.read-the-docs {
  color: #888;
}
</style>
