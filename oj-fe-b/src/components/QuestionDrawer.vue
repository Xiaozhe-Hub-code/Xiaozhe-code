<template>
    <el-drawer v-model="visibleDrawer" :destroy-on-close="true" :with-header="false" size="45%">
        <!-- <template #header>
            <h2>添加题目</h2>
        </template> -->
        <el-form :model="formModel" ref="formRef">

            <el-form-item label="题目标题:">
                <el-input maxlength="20" show-word-limit style="width:387px !important" v-model="formQuestion.title" placeholder="请输入标题"></el-input>
            </el-form-item>

            <el-form-item label="题目难度:">
                <QuestionSelector style="width:387px !important" v-model="formQuestion.difficulty" width="100%"
                    placeholder="请选择题目难度">
                </QuestionSelector>
            </el-form-item>

            <el-form-item label="时间限制（单位毫秒）:">
                <el-input style="width:300px !important" v-model="formQuestion.timeLimit"
                    placeholder="请输入时间限制"></el-input>
            </el-form-item>

            <el-form-item label="空间限制（单位字节）:">
                <el-input style="width:300px !important" v-model="formQuestion.spaceLimit"
                    placeholder="请输入空间限制"></el-input>
            </el-form-item>

            <el-form-item label="题目内容:">
                <div class="editor">
                    <quill-editor placeholder="请输入题目内容" v-model:content="formQuestion.content" content-type="html">
                    </quill-editor>
                </div>
            </el-form-item>

            <el-form-item label="题目用例">
                <el-input style="width:387px !important" v-model="formQuestion.questionCase"
                    placeholder="请输入题目用例"></el-input>
            </el-form-item>

            <el-form-item label="默认代码块:">
                <code-editor ref="defaultCodeRef" @update:value="handleEditorContent"></code-editor>
            </el-form-item>

            <el-form-item label="main函数:">
                <code-editor ref="mainFuncRef" @update:value="handleEditorMainFunc"></code-editor>
            </el-form-item>

            <el-form-item>
                <el-button class="question-button" type="primary" plain @click="onSubmit()">发布</el-button>
            </el-form-item>

        </el-form>
    </el-drawer>
</template>

<script setup>
import { QuillEditor } from '@vueup/vue-quill';
import '@vueup/vue-quill/dist/vue-quill.snow.css';
import CodeEditor from './CodeEditor.vue';
import QuestionSelector from './QuestionSelector.vue';
import { reactive, ref } from 'vue';
import { ElMessage } from 'element-plus';
import { addQuestionService, editQuestionService, getQuestionDetailService } from '@/apis/question';

const visibleDrawer = ref(false);

const formQuestion = reactive({
    questionId: '',
    title: '',
    difficulty: '',
    timeLimit: '',
    spaceLimit: '',
    content: '',
    questionCase: '',
    defaultCode: '',
    mainFunc: '',
})

const defaultCodeRef = ref();
const mainFuncRef = ref();

async function open(questionId) {
    visibleDrawer.value = true;
    for (const key in formQuestion) {
        if (formQuestion.hasOwnProperty(key)) {
            formQuestion[key] = '';
        }
    }
    if (questionId) {
        console.log("questionId: " + questionId);
        const questionDetail = await getQuestionDetailService(questionId);
        Object.assign(formQuestion, questionDetail.data);

        defaultCodeRef.value.setAceCode(formQuestion.defaultCode);
        mainFuncRef.value.setAceCode(formQuestion.mainFunc);
    }
}

defineExpose({
    open
})

function validate() {
    let msg = ''
    if (!formQuestion.title) {
        msg = '请添加题目标题'
    } else if (formQuestion.difficulty == '') {
        msg = '请选择题目难度'
    } else if (!formQuestion.timeLimit) {
        msg = '请输入时间限制'
    } else if (!formQuestion.spaceLimit) {
        msg = '请输入空间限制'
    } else if (!formQuestion.content) {
        msg = '请输入题目内容信息'
    } else if (!formQuestion.questionCase) {
        msg = '请输入题目用例名称'
    } else if (!formQuestion.defaultCode) {
        msg = '请输入默认代码'
    } else if (!formQuestion.mainFunc) {
        msg = '请输入main函数'
    } else {
        msg = ''
    }
    return msg
}

const emit = defineEmits(['success']);

async function onSubmit() {
    const errorMessage = validate();
    if (errorMessage) {
        ElMessage.error(errorMessage);
        return false;
    }
    const fd = new FormData();
    for (let key in formQuestion) {
        fd.append(key, formQuestion[key]);
    }
    // 判断是发布还是编辑 如果是发布一定不会有questionId属性
    if (formQuestion.questionId) {
        // 编辑
        await editQuestionService(fd);
        ElMessage.success('编辑成功');
        emit('success','edit');
        visibleDrawer.value = false;
    } else {
        // 发布
        await addQuestionService(fd);
        ElMessage.success('添加成功');
        emit('success','add');
        visibleDrawer.value = false;
    }

}

function handleEditorContent(content) {
    formQuestion.defaultCode = content;
}

function handleEditorMainFunc(content) {
    formQuestion.mainFunc = content;
}

</script>

<style lang="scss">
.question-button {
    width: 200px;
}
</style>
