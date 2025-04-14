import{p as oe,r as V,aE as ue,ai as d,q as a,t as _,U as t,O as e,I as re,u as U,T as c,R as m,S as p,M as y,F as $,a8 as E,v}from"./vue-956193f0.js";import{b as ce,a as ie,c as de}from"./level-61112490.js";import{H as _e,p as pe,W as ve,a as fe,b as me}from"./level.css_vue_type_style_index_0_src_true_lang-509bbc79.js";import{q as he,s as be,t as ye}from"./index-46bdc1ae.js";import{C as ge,E as R,D as ke}from"./element-0afb0e99.js";import{e as we,f as xe}from"./index-35ab6744.js";import"./vxe-78a23074.js";const Te="/static/sanitizer_icon-58a9c1c4.svg";function W(g){const i=Object.entries(g).sort(([n],[b])=>n==="host"||n==="referer"?-1:b==="host"||b==="referer"?1:n.localeCompare(b));let w="";return i.forEach(([n,b])=>{n=n.replace(/(?:^|-)([a-z])/g,T=>T.toUpperCase()),w=w+n+": "+b+`\r
`}),w}function Ve(g,i,w,n,b){let T=W(w);i=i.toUpperCase();let C=`${i} ${g} ${b}\r
`;return C+=T,n!=null&&(C+=`\r
${atob(n)}`),C}function Ce(g,i,w){let n=W(w);return`${g} ${i}\r
${n}`}function Re(g){return g.className+"."+g.method+g.desc}const Se={key:0},Le={key:1},qe={key:2},De={key:0},Ue=v("strong",null,"所属应用:",-1),He={key:0},Ie=v("strong",null,"首次发现时间:",-1),Ne={key:0},$e=v("strong",null,"最新发现时间:",-1),Ee={class:"custom-label"},Oe={key:0},Pe={class:"custom-parent-label"},ze=v("hr",null,null,-1),je={class:"parent-node-sign"},Ae={class:"left-content"},Be={class:"right-content"},Fe={key:0,src:Te,alt:""},Me={key:1},We={key:2},Qe={class:"sanitizer-text-container"},Je={key:3},Ke={style:{display:"inline-block","font-weight":"bold",width:"80px","text-align":"right"}},Ze={style:{display:"inline-block","background-color":"#dedfe080",width:"100%","padding-left":"10px","border-radius":"5px"}},Ge={class:"http-container"},Xe={class:"http-left-pane"},Ye=v("h3",null,"HTTP请求",-1),et={class:"http-right-pane"},tt=v("h3",null,"HTTP响应",-1),ct=oe({__name:"detail",setup(g){const i=V(),w=ue(),n=V(),b=V(),T=V(),C=V("first"),s=V(),H=V(),Q=()=>new Promise((l,r)=>{be(w.currentRoute.value.params.id).then(o=>{s.value=o.data,l(o.data)}).catch(o=>{r(o)})});function J(){navigator.clipboard.writeText(b.value).then(()=>{R.success("文本已成功复制到剪贴板")}).catch(l=>{R.error("复制到剪贴板失败: "+l.message)})}function K(){navigator.clipboard.writeText(T.value).then(()=>{R.success("文本已成功复制到剪贴板")}).catch(l=>{R.error("复制到剪贴板失败: "+l.message)})}function Z(){return s.value}function G(){ye().then(l=>{H.value=l.data})}async function X(){try{await Q(),Y(),G()}catch(l){console.error("获取数据失败:",l)}}function Y(){const l=Z(),r=JSON.parse(l.findingData);let o=[];const u=r.vulnerableType;if(u==_e)i.value=2,o=pe(r);else if(u==ve)i.value=3,o=fe(r),O(l);else{i.value=1;const k=r.flowData;o=me(k),O(l)}n.value=o}function O(l){b.value=Ve(l.uri,l.requestMethod,l.requestHeader,l.requestBody,l.protocol),T.value=Ce(l.protocol,l.statusCode,l.responseHeader)}function ee(l){let r=s.value.id;he(r,l).then(()=>{let o=H.value.find(u=>u.statusId===l);R.success("漏洞状态已切换成"+o.statusName)}).catch(o=>{ke(o)})}function te(){const l=`/api/v1/report/exportOne?reportId=${s.value.id}`,r=we();xe({method:"get",baseURL:l,data:{},headers:{Authorization:r?`Bearer ${r}`:void 0},responseType:"blob"}).then(o=>{const u=o.data;if(!u)return;const k=window.URL.createObjectURL(new Blob([u])),x=document.createElement("a");x.style.display="none",x.href=k,x.setAttribute("download","report-"+de()+".json"),document.body.appendChild(x),x.click(),window.URL.revokeObjectURL(x.href),document.body.removeChild(x)}).catch(o=>{R.error("导出失败,原因:"+o)})}return X(),(l,r)=>{const o=d("el-card"),u=d("el-text"),k=d("el-col"),x=d("el-option"),ae=d("el-select"),I=d("el-button"),N=d("el-tooltip"),L=d("el-row"),P=d("el-collapse-item"),z=d("el-collapse"),le=d("el-tree"),ne=d("el-header"),S=d("el-table-column"),j=d("el-table"),A=d("el-main"),B=d("el-container"),F=d("el-tab-pane"),M=d("el-input"),se=d("el-tabs");return a(),_("div",null,[t(o,{shadow:"hover",class:"report-card"},{default:e(()=>[s.value?(a(),_("h3",Se,[s.value?(a(),_("span",{key:0,class:re(U(ce)(s.value.level)),style:{"margin-right":"10px"}},c(U(ie)(s.value.level)),3)):m("",!0),i.value==1||i.value==3?(a(),_("span",Le,c(s.value.url),1)):i.value==2?(a(),_("span",qe,c(n.value[0].sourceFile),1)):m("",!0),p(" 发现 "+c(s.value.vulnerableTypeZH),1)])):m("",!0)]),_:1}),t(o,{class:"report-card",shadow:"hover"},{default:e(()=>[t(L,{gutter:10},{default:e(()=>[t(k,{span:6},{default:e(()=>[s.value?(a(),_("div",De,[Ue,s.value.agent?(a(),y(u,{key:0},{default:e(()=>[p(c(s.value.agent.hostname),1)]),_:1})):(a(),y(u,{key:1},{default:e(()=>[p("未知")]),_:1}))])):m("",!0)]),_:1}),t(k,{span:6},{default:e(()=>[s.value?(a(),_("div",He,[Ie,s.value.firstTimestamp?(a(),y(u,{key:0},{default:e(()=>[p(c(s.value.firstTimestamp),1)]),_:1})):m("",!0)])):m("",!0)]),_:1}),t(k,{span:6},{default:e(()=>[s.value?(a(),_("div",Ne,[$e,s.value.lastTimestamp?(a(),y(u,{key:0},{default:e(()=>[p(c(s.value.lastTimestamp),1)]),_:1})):m("",!0)])):m("",!0)]),_:1}),t(k,{span:6},{default:e(()=>[t(L,{gutter:10},{default:e(()=>[t(k,{span:8},{default:e(()=>[s.value?(a(),y(ae,{key:0,modelValue:s.value.status.statusId,"onUpdate:modelValue":r[0]||(r[0]=f=>s.value.status.statusId=f),onChange:ee},{default:e(()=>[(a(!0),_($,null,E(H.value,f=>(a(),y(x,{label:f.statusName,value:f.statusId,key:f.value},null,8,["label","value"]))),128))]),_:1},8,["modelValue"])):m("",!0)]),_:1}),t(k,{span:6},{default:e(()=>[t(N,{content:"导出漏洞"},{default:e(()=>[t(I,{type:"primary",icon:U(ge),circle:"",onClick:te},null,8,["icon"])]),_:1})]),_:1})]),_:1})]),_:1})]),_:1})]),_:1}),t(o,{class:"report-details",shadow:"hover"},{default:e(()=>[t(se,{modelValue:C.value,"onUpdate:modelValue":r[3]||(r[3]=f=>C.value=f)},{default:e(()=>[t(F,{label:"漏洞细节",name:"first"},{default:e(()=>[i.value==1?(a(),y(le,{key:0,indent:16,data:n.value,"node-key":"id"},{default:e(({node:f,data:h})=>[v("div",Ee,[f.level==1?(a(),_("div",Oe,[v("div",Pe,c(h.label),1),ze,v("div",je,[v("div",Ae,c(h.sign),1),v("div",Be,[t(L,{class:"w-600px mb-2"},{default:e(()=>[t(N,{content:h.taint,placement:"top-end"},{default:e(()=>[t(u,{style:{color:"red"},truncated:""},{default:e(()=>[p(c(h.taint),1)]),_:2},1024)]),_:2},1032,["content"]),h.isSanitizer?(a(),_("img",Fe)):m("",!0)]),_:2},1024)])])])):h.name=="stack"?(a(),_("div",Me,[t(z,null,{default:e(()=>[t(P,{title:"调用栈"},{default:e(()=>[h.value?(a(!0),_($,{key:0},E(h.value,(q,D)=>(a(),_("div",{key:D},[t(u,{truncated:"",style:{"line-height":"30px","padding-left":"10px","background-color":"rgba(222, 223, 224, 0.5)"}},{default:e(()=>[p(c(q),1)]),_:2},1024)]))),128)):m("",!0)]),_:2},1024)]),_:2},1024)])):h.name=="sanitizer"?(a(),_("div",We,[t(z,null,{default:e(()=>[t(P,{title:"过滤函数"},{default:e(()=>[h.value?(a(!0),_($,{key:0},E(h.value,(q,D)=>(a(),_("div",{key:D},[v("div",Qe,[v("span",null,c(D+1)+".",1),t(u,{class:"left-text"},{default:e(()=>[p(c(U(Re)(q)),1)]),_:2},1024),t(u,{class:"right-red-text"},{default:e(()=>[p(c(q.toValue),1)]),_:2},1024)])]))),128)):m("",!0)]),_:2},1024)]),_:2},1024)])):(a(),_("div",Je,[v("div",Ke,c(h.label),1),v("div",Ze,[t(L,{class:"w-1200px mb-2"},{default:e(()=>[t(N,{content:h.value,placement:"top-end"},{default:e(()=>[t(u,{truncated:""},{default:e(()=>[p(c(h.value),1)]),_:2},1024)]),_:2},1032,["content"])]),_:2},1024)])]))])]),_:1},8,["data"])):i.value==2?(a(),y(B,{key:1},{default:e(()=>[n.value?(a(),y(ne,{key:0},{default:e(()=>[p(" 硬编码漏洞存在于 "),t(u,{tag:"b"},{default:e(()=>[p(c(n.value[0].sourceFile),1)]),_:1}),p(" 文件中, 类路径： "),t(u,{tag:"b"},{default:e(()=>[p(c(n.value[0].className),1)]),_:1})]),_:1})):m("",!0),t(A,null,{default:e(()=>[n.value?(a(),y(j,{key:0,data:n.value,style:{width:"100%"}},{default:e(()=>[t(S,{prop:"parameterName",label:"参数名"}),t(S,{label:"参数值"},{default:e(f=>[t(u,null,{default:e(()=>[p(c(f.row.parameterValue),1)]),_:2},1024)]),_:1})]),_:1},8,["data"])):m("",!0)]),_:1})]),_:1})):i.value==3?(a(),y(B,{key:2},{default:e(()=>[t(A,null,{default:e(()=>[n.value?(a(),y(j,{key:0,data:n.value,style:{width:"100%"}},{default:e(()=>[t(S,{prop:"url",label:"数据库连接地址"}),t(S,{prop:"user",label:"用户名"}),t(S,{prop:"password",label:"密码"},{default:e(f=>[t(u,null,{default:e(()=>[p(c(f.row.password),1)]),_:2},1024)]),_:1})]),_:1},8,["data"])):m("",!0)]),_:1})]),_:1})):m("",!0)]),_:1}),i.value==1||i.value==3?(a(),y(F,{key:0,label:"HTTP请求信息",name:"second"},{default:e(()=>[v("div",Ge,[v("div",Xe,[Ye,t(M,{type:"textarea",modelValue:b.value,"onUpdate:modelValue":r[1]||(r[1]=f=>b.value=f)},null,8,["modelValue"]),t(I,{class:"http-copy-button",onClick:J},{default:e(()=>[p("复制")]),_:1})]),v("div",et,[tt,t(M,{type:"textarea",modelValue:T.value,"onUpdate:modelValue":r[2]||(r[2]=f=>T.value=f)},null,8,["modelValue"]),t(I,{class:"http-copy-button",onClick:K},{default:e(()=>[p("复制")]),_:1})])])]),_:1})):m("",!0)]),_:1},8,["modelValue"])]),_:1})])}}});export{ct as default};
