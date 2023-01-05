import { createApp } from 'vue';
import 'normalize.css';
// import './style.css';
import App from './App.vue';
import { setupRouter } from './router';

// import 'virtual:windi-base.css';
// import 'virtual:windi-components.css';
// import 'virtual:windi-utilities.css';
import 'virtual:windi-devtools';

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
