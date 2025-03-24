import axios from "axios"
import { getToken, removeToken } from "./cookie";
import router from "@/router";

// axios请求
axios.defaults.headers.post["Content-Type"] = "application/json;charset=utf-8"; // 设置post请求头
axios.defaults.headers.put["Content-Type"] = "application/json;charset=utf-8"; // 设置put请求头
const service = axios.create({
    // baseURL : "http://127.0.0.1:19090/system", // system接口下的公共url
    baseURL : "/dev-api",
    timeout : 5000, // 超时时间5s（5s之内返回响应正常，反之不正常，返回错误信息）
});

export default service; // 给全局都能调用service



//  请求拦截器(前端返回到后端之前执行的逻辑)
service.interceptors.request.use(
    (config) => {
        if (getToken()) {
            config.headers["Authorization"] = "Bearer " + getToken(); // 注意有空格和后端一致，不得修改
        }
        return config;
    },
    (error) => {
        console.log(error);
        Promise.reject(error);
    }
)


//  响应拦截器(后端返回到前端之前执行的逻辑)
//  通过axios请求后，进行前端拦截
service.interceptors.response.use(
    (res) => {
        // 未设置状态码则默认成功状态
        const code = res.data.code;
        const msg = res.data.msg;
        // 后端token如果过期则返回3001错误码，捕捉之后前端返回登陆界面
        if (code == 3001) {
            ElMessage.error(msg);
            // 删掉前端过期token
            removeToken();
            router.push('/oj/login');
            return Promise.reject(new Error(msg));
        }
        else if (code != 1000) {
            ElMessage.error(msg);
            return Promise.reject(new Error(msg));
        } else {
            // 这里是 Promise.resolve() 表示成功
            // 刚才是 Promise.reject()意味着失败了
            return Promise.resolve(res.data);
        }
    },
    (error) => {
        return Promise.reject(error);
    }
);
