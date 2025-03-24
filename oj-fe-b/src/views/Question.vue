<template>
    <el-form inline="true">

        <el-form-item>
            <selector v-model="params.difficulty" placeholder="请选择题目难度" style="width: 200px;"></selector>
        </el-form-item>

        <el-form-item>
            <el-input v-model="params.title" placeholder="请输入要搜索的题目标题" />
        </el-form-item>

        <el-form-item>
            <el-button plain @Click="onSerch">搜索</el-button>
            <el-button plain @Click="onReset" type="info">重置</el-button>
            <el-button plain @Click="onAddQuestion" type="primary" :icon="Plus">添加题目</el-button>
        </el-form-item>

    </el-form>

    <el-table height="526px" :data="questionList">

        <el-table-column prop="questionId" width="180px" label="题目id" />
        <el-table-column prop="title" label="题目标题" />
        <el-table-column prop="difficulty" label="题目难度" width="90px">
            <template #default="{ row }">
                <div v-if="row.difficulty === 1" style="color:#3EC8FF;">简单</div>
                <div v-if="row.difficulty === 2" style="color:#FE7909;">中等</div>
                <div v-if="row.difficulty === 3" style="color:#FD4C40;">困难</div>
            </template>
        </el-table-column>

        <el-table-column prop="createName" label="创建⼈" width="140px" />
        <el-table-column prop="createTime" label="创建时间" width="180px" />
        <el-table-column label="操作" width="100px" fixed="right">
            <template #default="{ row }">
                <el-button @Click="onEdit(row.questionId)" type="text">编辑
                </el-button>
                <el-button type="text" class="red" @click="onDelete(row.questionId)">删除
                </el-button>
            </template>
        </el-table-column>

    </el-table>
    <!-- 分页器 -->
    <el-pagination background size="small" layout="total, sizes, prev, pager, next, jumper" :total="total"
        v-model:current-page="params.pageNumber" v-model:page-size="params.pageSize" :page-sizes="[1, 3, 5, 10, 15, 20]"
        @size-change="handleSizeChange" @current-change="handleCurrentChange" />

    <question-drawer ref="questionDrawerRef" @success="onSuccess" />

</template>

<script setup>
import { Plus } from "@element-plus/icons-vue"
import Selector from "@/components/QuestionSelector.vue"
import { deleteQuestionService, getQuestionDetailService, getQuestionListService } from "@/apis/question"
import { reactive, ref } from "vue";
import QuestionDrawer from "@/components/QuestionDrawer.vue";

// 定义params（传参）
const params = reactive({
    pageNumber: 1,
    pageSize: 10,
    difficulty: '',
    title: ''
})
const questionList = ref([]);
const total = ref(0);

async function getQuestionList() {
    const result = await getQuestionListService(params);
    questionList.value = result.rows;
    total.value = result.total;
}
getQuestionList();

function handleSizeChange(newSize) {
    // params.pageSize = newSize;
    params.pageNumber = 1;
    // console.log("pageSize:" +params.pageSize);
    // console.log("pageNumber:" +params.pageNumber);
    getQuestionList();

}

function handleCurrentChange(newPage) {
    // params.pageNumber = newPage;
    // console.log("pageNum:" +params.pageNumber);
    getQuestionList();
}

function onSerch() {
    params.pageNumber = 1;
    getQuestionList();
}

function onReset() {
    params.pageNumber = 1;
    params.pageSize = 10;
    params.difficulty = '';
    params.title = '';
    getQuestionList();
}

const questionDrawerRef = ref();

function onAddQuestion() {
    questionDrawerRef.value.open();
}

function onSuccess(service) {
    if (service == 'add') {
        params.pageNumber = 1;
    }
    getQuestionList();
}

async function onEdit(questionId) {
    questionDrawerRef.value.open(questionId);
}

async function onDelete(questionId) {
    console.log("questionId: " + questionId);
    await deleteQuestionService(questionId);
    getQuestionList();
    ElMessage.success('删除成功');
}
</script>