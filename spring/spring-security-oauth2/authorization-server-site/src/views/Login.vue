<script setup lang="ts"></script>

<template>
  <div>
    <form action="/login/form" method="post">
      <div class="input">
        <label for="username">用户名</label>
        <input type="text" name="username" id="username" required />
        <span class="spin"></span>
      </div>
      <div class="input">
        <label for="password">密码</label>
        <input type="password" name="password" id="password" required />
        <span class="spin"></span>
      </div>
      <div class="button login">
        <button type="submit">
          <span>登录</span>
          <i class="fa fa-check"></i>
        </button>
      </div>
    </form>
  </div>
</template>

<style scoped></style>
