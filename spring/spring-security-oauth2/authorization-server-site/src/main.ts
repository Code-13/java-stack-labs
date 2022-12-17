import { createApp } from 'vue';
import './style.css';
import App from './App.vue';
import { setupRouter } from './router';

async function bootstrap() {
  const app = createApp(App);

  // Configure routing
  // 配置路由
  setupRouter(app);

  // https://next.router.vuejs.org/api/#isready
  // await router.isReady();

  app.mount('#app');
}

bootstrap();
