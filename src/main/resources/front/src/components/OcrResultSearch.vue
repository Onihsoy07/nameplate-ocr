<template>
    <div>
        <input type="date" v-model="data.startDate" />
        <input type="date" v-model="data.endDate" />
        <input type="text" v-model="data.lineName" />
        <input type="text" v-model="data.correctData" />
        <select v-model="data.checkResult">
            <option value="null">ALL</option>
            <option value="OK">OK</option>
            <option value="NG">NG</option>
        </select>

        <button @click="ocrSearch()">검색</button>
    </div>
</template>

<script setup>
import { reactive } from 'vue';

const data = reactive({
    startDate: '',
    endDate: '',
    lineName: '',
    correctData: '',
    checkResult: null
});

const ocrSearch = () => {
    console.log(data.checkResult)
};

</script>

<style scoped>

</style>