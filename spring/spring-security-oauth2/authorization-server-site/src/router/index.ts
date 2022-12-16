import {createRouter, createWebHistory} from "vue-router";
import type {App} from "vue";
import routes from "./routes";

// app router
// 创建一个可以被 Vue 应用程序使用的路由实例
export const router = createRouter({
  // 创建一个 hash 历史记录。
  history: createWebHistory(import.meta.env.VITE_PUBLIC_PATH),
  // 应该添加到路由的初始路由列表。
  routes: routes,
  // 是否应该禁止尾部斜杠。默认为假
  strict: true,
  scrollBehavior: () => ({left: 0, top: 0}),
});

// reset router
export function resetRouter() {
  router.getRoutes().forEach((route) => {
    const {name} = route;
    if (name) {
      router.hasRoute(name) && router.removeRoute(name);
    }
  });
}

// config router
// 配置路由器
export function setupRouter(app: App<Element>) {
  app.use(router);
}
