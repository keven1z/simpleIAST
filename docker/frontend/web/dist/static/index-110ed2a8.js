import{k as e}from"./index-e50c813d.js";function n(){return e({url:"/notify/unread/count",method:"get"})}function u(t){return e({url:"/notify/all",method:"get",params:t})}function o(t){return e({url:`/notify/read/set?id=${t}`,method:"get"})}function s(){return e({url:"/notify/unread/list",method:"get"})}function d(t){return e({url:"/agent/list",method:"get",params:t})}function l(){return e({url:"/agent/getAgentKey",method:"get"})}function i(t,r){return e({url:`/agent/queryByHostName?name=${t}&state=${r}`,method:"get"})}function g(t){return e({url:`/agent/blacklist/search?name=${t}`,method:"get"})}function p(t){return e({url:"/agent/blacklist/all",method:"get",params:t})}function m(t){return e({url:"/agent/blacklist/add",method:"post",data:t})}function c(t){return e({url:`/agent/blacklist/del?name=${t}`,method:"get"})}function f(t){return e({url:"/app/list",method:"get",params:t})}function h(t){return e({url:`/app/del?ids=${t}`,method:"get"})}function y(t){return e({url:`/app/queryByName?name=${t}`,method:"get"})}function q(t){return e({url:"/app/add",method:"post",data:t})}function D(t){return e({url:"/user/add",method:"post",data:t})}function A(t){return e({url:"/user/update",method:"post",data:t})}function $(t){return e({url:`/user/delete?id=${t}`,method:"get"})}function B(){return e({url:"/report/getReportTOP5",method:"get"})}function L(){return e({url:"/report/getLast12MonthReport",method:"get"})}function b(){return e({url:"/report/getLast7DayReport",method:"get"})}function k(t){return e({url:"/report/list",method:"get",params:t})}function R(t,r){return e({url:`/report/getReportByLevelAndAgentId?agentId=${t}&level=${r}`,method:"get"})}function N(t){return e({url:`/report/get/?id=${t}`,method:"get"})}function U(t){return e({url:`/report/delete?ids=${t}`,method:"get"})}function v(t){return e({url:`/agent/delete?ids=${t}`,method:"get"})}function I(t){return e({url:"/user/list",method:"get",params:t})}function O(t){return e({url:"/log/list",method:"get",params:t})}function V(){return e({url:"/app/getDashboardData",method:"get"})}function x(){return e({url:"/agent/getDashboardData",method:"get"})}function C(){return e({url:"/report/getDashboardData",method:"get"})}export{u as A,I as B,A as C,D,$ as E,n as a,s as b,l as c,V as d,C as e,x as f,f as g,B as h,b as i,L as j,h as k,q as l,y as m,d as n,v as o,i as p,N as q,o as r,k as s,U as t,R as u,m as v,p as w,c as x,g as y,O as z};