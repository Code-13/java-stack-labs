/*
 * Copyright 2022-present the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import type {AxiosResponse} from "axios";
import axios from "axios";

const service = axios.create({
  baseURL: import.meta.env.VITE_APP_BASE_URL,
  withCredentials: true
})

service.interceptors.response.use(
    response => response,
    error => {
      if (error instanceof Error) {
        if (error.message === 'Network Error') {
          // alert('网络错误请稍后重试')
        }
      }

      if (!error.response) {
        return error;
      }

      const response = error.response as AxiosResponse

      switch (response.status) {
        case 401: {
          window.location.href = `${import.meta.env.VITE_APP_SSO_LOGIN_URL}?redirectUrl=${encodeURIComponent(window.location.href)}`
          return Promise.reject(error);
        }
        default: {
          // Toast 提示信息
          return Promise.reject(error);
        }
      }

    })

export default service