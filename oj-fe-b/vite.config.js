import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },

  // 解决跨域问题  在request.js 中   baseURL : "http://127.0.0.1:19090/system"  =>  baseURL : "/dev-api"
  server: {
    proxy: {
      "/dev-api" : {
        target:  "http://127.0.0.1:19090/system",
        rewrite: (p) => p.replace(/^\/dev-api/,""),
      },
    },
  },
})
