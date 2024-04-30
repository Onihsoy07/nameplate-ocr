import { createWebHistory, createRouter } from "vue-router";


const router = createRouter({
    history: createWebHistory(),
    routes: [
        {
            path: "/",
            name: "index",
            component: () => import("@/components/MainHome.vue")
        },
        {
            path: "/ocr-search",
            name: "ocr-search",
            component: () => import("@/components/OcrResultSearch.vue")
        },
        {
            path: "/ocr/:ocrResultId",
            name: "ocr-result",
            component: () => import("@/components/OcrInfo.vue")
        },

    ]
});

export default router;