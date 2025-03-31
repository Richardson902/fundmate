import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import dns from "node:dns";

dns.setDefaultResultOrder("verbatim");

export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      "/api/": {
        target: "http://fundmate-api:8080",
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
