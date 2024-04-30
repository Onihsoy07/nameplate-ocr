<template>
    <div>
        <h1>명판 OCR</h1>
        <div class="data-outer">
            <img class="original-image" :src="data.byteImage" alt="Image" />
            <div class="data-inner">
                <div class="data-wrap">
                    <p>라인</p>
                    <p>{{ data.lineName }}</p>
                </div>
                <div class="data-wrap">
                    <p>OCR 결과</p>
                    <p>{{ data.ocrText }}</p>
                </div>
                <div class="data-wrap">
                    <p>확인 정보</p>
                    <p>{{ data.correctData }}</p>
                </div>
                <div class="data-wrap">
                    <p>합불 판정</p>
                    <p>{{ data.checkResult }}</p>
                </div>
                <div class="data-wrap">
                    <p>검사 날짜</p>
                    <p>{{ data.imageCreateDate }}</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, reactive } from 'vue';
import { useRoute } from 'vue-router';
import axios from 'axios';

const route = useRoute();
const data = reactive({
    byteImage: '',
    lineName: '',
    correctData: '',
    checkResult: '',
    ocrText: '',
    imageCreateDate: '',

});

// 경로의 id를 확인하여 OCR 결과 데이터 받기
const getOcrResult = () => {
    axios({
        method: 'get',
        url: '/api/ocr/' + route.params.ocrResultId,
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((res) => {
        console.log(res);
        if (res.data.success) {
            console.log(res);

            data.lineName = res.data.data.lineName;
            data.correctData = res.data.data.correctData;
            data.checkResult = res.data.data.checkResult;
            data.ocrText = res.data.data.ocrResultText;
            data.imageCreateDate = res.data.data.imageCreateDate;
            data.byteImage = `data:image/jpeg;base64,${res.data.data.imageStream}`;
        } else {
            console.log(res.data);
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
        alert(error.response.data.message);
    });
};

// 이 페이지 마운트 시 호출
onMounted(() => {
    getOcrResult();
});

</script>

<style scoped>
.original-image {
    width: auto;
    height: 450px;
}
.data-outer {
    display: block;
}
.data-inner {
    border: solid 1px black;
    width: 500px;
    margin: 0 auto;
}
.data-wrap {
    border: solid 1px black;

}
.data-wrap p {
    all: unset;
    width: 50%;
    vertical-align: middle;
    display: inline-block;
    line-height: 25px;
    margin: 10px 0px;
}
</style>