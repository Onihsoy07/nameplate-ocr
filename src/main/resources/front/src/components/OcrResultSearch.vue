<template>
    <div>
        <input type="date" v-model="data.startDate" />
        <input type="date" v-model="data.endDate" />
        <input type="text" v-model="data.lineName" />
        <input type="text" v-model="data.correctData" />
        <select v-model="data.checkResult">
            <option value="">ALL</option>
            <option value="OK">OK</option>
            <option value="NG">NG</option>
        </select>

        <button @click="ocrSearch()">검색</button>
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

const data = reactive({
    startDate: '',
    endDate: '',
    lineName: '',
    correctData: '',
    checkResult: '',
    searchResult: {},

});

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
    window.open('/ocr/' + ocrResultId, '_blank', 'width=900px,height=700px')
}

</script>

<style scoped>
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
</style>