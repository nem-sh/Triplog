import axios from "axios";

// axios 객체 생성
export default axios.create({
  baseURL: "http://i3b207.p.ssafy.io:8080/api",
  headers: {
    "Content-type": "application/json"
  }
});
