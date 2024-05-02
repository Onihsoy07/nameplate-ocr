<template>
    <!-- <div v-if="data.detailOpen">
        <div class="background-black" @click="modalClose()"></div>
        <div class="modal-window">
            <button @click="modalClose()" class="btn-close">X</button>
            <OcrInfo :ocr-result-id="data.ocrResultId"></OcrInfo>
        </div>
    </div> -->

    <div class="search-params">
        <div>
            <label for="startDate">시작 날짜 </label>
            <input type="date" v-model="data.startDate" />
        </div>
        <div>
            <label for="endDate">끝 날짜 </label>
            <input type="date" v-model="data.endDate" />
        </div>
        <div>
            <label for="lineName">라인 </label>
            <input type="text" v-model="data.lineName" />
        </div>
        <div>
            <label for="correctData">확인 정보 </label>
            <input type="text" v-model="data.correctData" />
        </div>
        <label for="checkResult">합불 판정 </label>
        <select v-model="data.checkResult">
            <option value="">ALL</option>
            <option value="OK">OK</option>
            <option value="NG">NG</option>
        </select>
        <div>
            <button @click="ocrSearch()">검색</button>
        </div>
    </div>
    <div class="data-list-outer">
        <div class="data-list-inner">
            <div class="data-table-wrap">
                <table class="data-list-table">
                    <tr>
                        <th class="table-line-name">라인</th>
                        <th class="table-correct-data">확인 정보</th>
                        <th class="table-check-result">합불 판정</th>
                        <th class="table-check-date">검사 날짜</th>
                        <th class="table-detail-view">상세 확인</th>
                    </tr>
                </table>
            </div>
            <div class="data-table-wrap data-table">
                <table class="data-list-table">
                    <tr v-for="(resultData, idx) in data.searchResult" :key="idx">
                        <td class="table-line-name">{{ resultData.lineName }}</td>
                        <td class="table-correct-data">{{ resultData.correctData }}</td>
                        <td class="table-check-result">{{ resultData.checkResult }}</td>
                        <td class="table-check-date">{{ resultData.imageCreateDate }}</td>
                        <td class="table-detail-view"><button @click="detailView(resultData.id)">상세</button></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive } from 'vue';
import axios from 'axios';
// import OcrInfo from './OcrInfo.vue';

const data = reactive({
    startDate: '',
    endDate: '',
    lineName: '',
    correctData: '',
    checkResult: '',
    searchResult: {},
    detailOpen: false,


});

// OCR 결과 데이터 검색
const ocrSearch = () => {
    var searchParams = '';

    if (data.startDate != '') {
        searchParams += ('&startDate=' + data.startDate + '_00:00:00')
    }
    if (data.endDate != '') {
        searchParams += ('&endDate=' + data.endDate + '_23:59:59')
    }
    if (data.lineName != '') {
        searchParams += ('&lineName=' + data.lineName);
    }
    if (data.correctData != '') {
        searchParams += ('&correctData=' + data.correctData);
    }
    if (data.checkResult != '') {
        searchParams += ('&checkResult=' + data.checkResult);
    }

    axios({
        method: 'get',
        url: '/api/ocr?' + searchParams,
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((res) => {
        if (res.data.success) {
            data.searchResult = res.data.data;
            console.log(data.searchResult);
        } else {
            console.log(res.data);
            alert(res.data.message);
        }
    }).catch((error) => {
        console.log(error);
        alert(error.response.data.message);
    });
};
const detailView = (ocrResultId) => {
    // console.log(ocrResultId);
    // data.ocrResultId = ocrResultId;
    // data.detailOpen = true;
    window.open('/ocr/' + ocrResultId, '_blank', 'width=900px,height=700px')
};
// const modalClose = () => {
//     data.detailOpen = false;
// }

</script>

<style scoped>
.search-params div {
    margin: 7px 0px;
}
.data-list-outer {
    display: flex;
    margin-top: 30px;
}
.data-list-inner {
    margin: 0 auto;
}
.data-table-wrap {
    display: block;
}
.data-table {
    max-height: 500px;
    overflow-y: auto;
}
.data-list-table {
    border: solid 1px black;
    border-collapse: collapse;
}
.data-list-table th {
    border: solid 1px black;
    padding: 8px 0px;
}
.data-list-table td {
    border: solid 1px black;
    padding: 8px 0px;
}
.table-line-name {
    width: 100px;
}
.table-correct-data {
    width: 170px;
}
.table-check-result {
    width: 120px;
}
.table-check-date {
    width: 220px;
}
.table-detail-view {
    width: 100px;
}
.background-black {
    background-color: black;
    opacity : 0.4;
    position: absolute;
    top: 0px;
    left: 0px;
    width: 100%;
    height: 100%;
    z-index: 10;
}
.modal-window {
    position: fixed;
    left: calc(50% - 300px);
    top: 100px;
    width: 600px;
    height: 600px;
    background-color: white;
    overflow-y: auto;
    z-index: 99;
}
.btn-close {
    all: unset;
    float: right;
    padding: 15px 15px;
    font-size: 20px;
    cursor: pointer;
}
</style>