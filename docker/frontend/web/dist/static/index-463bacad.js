import{v as fe,t as me,w as ve,x as ge,q as we}from"./index-3d9e2404.js";import{x as he,r as be,z as ye,C as Ce,A as ke,B as De,E as T,D as Ve}from"./element-0afb0e99.js";import{p as Se,r as f,aE as xe,Z as Ie,ai as s,aq as ze,q as d,t as M,P as O,M as g,O as t,v as b,U as e,F as $,a8 as F,u as m,S as i,I as Ne,T as y,R as H}from"./vue-956193f0.js";import{v as Te,b as Re,a as Z,c as Le}from"./level-61112490.js";import{e as Ue,f as Be,_ as qe}from"./index-3236e402.js";import{p as Me,a as je,b as Ae}from"./level.css_vue_type_style_index_0_src_true_lang-fb9792d3.js";import"./vxe-78a23074.js";const Ee={class:"app-container"},Pe={class:"toolbar-wrapper"},Oe={class:"table-wrapper"},$e={class:"demo-pagination-block"},Fe=Se({__name:"index",setup(He){const C=f(!1),J=xe(),Q=f(!1),S=f(),R=f(10),L=f(1),U=f(0),z=f(0),w={pageSize:20,pageNum:1},c=Ie({agentId:"",level:0}),k=()=>{C.value=!0,fe(w).then(l=>{const{pageSize:n,pageNum:r,total:o,pages:p,list:u}=l.data;S.value=u,R.value=n,U.value=p,L.value=r,z.value=o}).catch(()=>{S.value=[]}).finally(()=>{C.value=!1})};function W(){me().then(l=>{q.value=l.data})}function D(l){const n=JSON.parse(l),r=n.vulnerableType;if(r=="hard_code")return Me(n)[0];if(r=="weak_password_in_sql")return je(n)[0];{const o=n.flowData,p=Ae(o),u=p.length;return p[u-1]}}function B(l){return l=="hard_code"?2:l=="weak_password_in_sql"?3:1}const j=f(),q=f();function G(l){j.value=l}function A(l){De.confirm("确定删除?","警告",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning",center:!0}).then(()=>{C.value=!0,ve(l).then(()=>{T({type:"success",message:"删除成功"}),k()}).finally(()=>{C.value=!1})}).catch(()=>{T({type:"info",message:"删除出错"})})}function K(){const l=[];for(const n of j.value)l.push(n.id);A(l.join(","))}function X(){const l="/api/v1/report/export",n=Ue();Be({method:"get",baseURL:l,data:{},headers:{Authorization:n?`Bearer ${n}`:void 0},responseType:"blob"}).then(r=>{const o=r.data;if(!o)return;const p=window.URL.createObjectURL(new Blob([o])),u=document.createElement("a");u.style.display="none",u.href=p,u.setAttribute("download","report-"+Le()+".json"),document.body.appendChild(u),u.click(),window.URL.revokeObjectURL(u.href),document.body.removeChild(u)}).catch(r=>{T.error("导出失败,原因:"+r)})}const Y=l=>{J.push(`/vuln/detail/${l}`)},ee=l=>{w.pageSize=l,k()},te=l=>{w.pageNum=l,k()},ae=()=>{const l=c.agentId,n=c.level;if(l==null||l==""&&n==0){k();return}ge(l,n).then(r=>{const o=r.data;S.value=o.list,R.value=o.pageSize,U.value=o.pages,L.value=o.pageNum,z.value=o.total}).catch(()=>{S.value=[],R.value=0,U.value=0,L.value=0,z.value=0})},le=()=>{c.agentId="",c.level=0};function ne(l,n){we(l,n).then(()=>{let r=q.value.find(o=>o.statusId===n);T.success("漏洞状态已切换成"+r.statusName)}).catch(r=>{Ve(r)})}return k(),W(),(l,n)=>{const r=s("el-input"),o=s("el-form-item"),p=s("el-option"),u=s("el-select"),x=s("el-button"),oe=s("el-form"),E=s("el-card"),V=s("el-tooltip"),h=s("el-table-column"),se=s("el-link"),_=s("el-col"),I=s("el-text"),N=s("el-row"),re=s("More"),ue=s("el-icon"),de=s("el-dropdown-item"),ie=s("el-dropdown-menu"),ce=s("el-dropdown"),pe=s("el-table"),_e=s("el-pagination"),P=ze("loading");return d(),M("div",Ee,[O((d(),g(E,{shadow:"never",class:"search-wrapper"},{default:t(()=>[b("div",null,[e(oe,{ref:"searchFormRef",inline:!0,model:c},{default:t(()=>[e(o,{prop:"url",label:"应用ID"},{default:t(()=>[e(r,{modelValue:c.agentId,"onUpdate:modelValue":n[0]||(n[0]=a=>c.agentId=a),placeholder:"请输入"},null,8,["modelValue"])]),_:1}),e(o,{prop:"level",label:"漏洞等级"},{default:t(()=>[e(u,{modelValue:c.level,"onUpdate:modelValue":n[1]||(n[1]=a=>c.level=a),style:{width:"80px"}},{default:t(()=>[(d(!0),M($,null,F(m(Te),a=>(d(),g(p,{label:m(Z)(a.value),value:a.value,key:a.value},null,8,["label","value"]))),128))]),_:1},8,["modelValue"])]),_:1}),e(o,null,{default:t(()=>[e(x,{type:"primary",icon:m(he),onClick:ae},{default:t(()=>[i("查询")]),_:1},8,["icon"]),e(x,{icon:m(be),onClick:le},{default:t(()=>[i("重置")]),_:1},8,["icon"])]),_:1})]),_:1},8,["model"])])]),_:1})),[[P,C.value]]),O((d(),g(E,{shadow:"never"},{default:t(()=>[b("div",Pe,[b("div",null,[e(x,{type:"danger",icon:m(ye),onClick:K},{default:t(()=>[i("批量删除")]),_:1},8,["icon"])]),b("div",null,[e(V,{content:"导出报告"},{default:t(()=>[e(x,{type:"primary",icon:m(Ce),circle:"",onClick:X},null,8,["icon"])]),_:1}),e(V,{content:"刷新当前页"},{default:t(()=>[e(x,{type:"primary",icon:m(ke),circle:"",onClick:k},null,8,["icon"])]),_:1})])]),b("div",Oe,[e(pe,{ref:"multipleTableRef",data:S.value,style:{width:"100%"},onSelectionChange:G},{default:t(()=>[e(h,{type:"selection",width:"55"}),e(h,{prop:"level",label:"漏洞等级",width:"120",sortable:""},{default:t(a=>[b("span",{class:Ne(m(Re)(a.row.level))},y(m(Z)(a.row.level)),3)]),_:1}),e(h,{label:"漏洞概述",align:"left",width:"600"},{default:t(a=>[e(_,null,{default:t(()=>[e(N,{span:5},{default:t(()=>[e(_,{span:8},{default:t(()=>[e(se,{type:"primary",onClick:v=>Y(a.row.id)},{default:t(()=>[i(y(a.row.vulnerableTypeZH),1)]),_:2},1032,["onClick"])]),_:2},1024),e(_,{span:16},{default:t(()=>[e(I,null,{default:t(()=>[i("keypoint")]),_:1})]),_:1})]),_:2},1024),B(a.row.vulnerableType)==1?(d(),g(N,{key:0,span:15},{default:t(()=>[e(_,{span:8},{default:t(()=>[e(V,{content:a.row.uri,placement:"bottom-end"},{default:t(()=>[e(I,{truncated:"",class:"v-descrption"},{default:t(()=>[i(y(a.row.requestMethod)+" "+y(a.row.uri),1)]),_:2},1024)]),_:2},1032,["content"])]),_:2},1024),e(_,{span:16},{default:t(()=>[e(V,{placement:"bottom-end",content:D(a.row.findingData).sign},{default:t(()=>[e(I,{truncated:"",class:"v-descrption"},{default:t(()=>[i(y(D(a.row.findingData).sign),1)]),_:2},1024)]),_:2},1032,["content"])]),_:2},1024)]),_:2},1024)):B(a.row.vulnerableType)==2?(d(),g(N,{key:1,span:15},{default:t(()=>[e(_,{span:8}),e(_,{span:16},{default:t(()=>[e(V,{placement:"bottom-end",content:D(a.row.findingData).className},{default:t(()=>[e(I,{truncated:"",class:"v-descrption"},{default:t(()=>[i(y(D(a.row.findingData).className),1)]),_:2},1024)]),_:2},1032,["content"])]),_:2},1024)]),_:2},1024)):B(a.row.vulnerableType)==3?(d(),g(N,{key:2,span:15},{default:t(()=>[e(_,{span:8}),e(_,{span:16},{default:t(()=>[e(V,{placement:"bottom-end",content:D(a.row.findingData).url},{default:t(()=>[e(I,{truncated:"",class:"v-descrption"},{default:t(()=>[i(y(D(a.row.findingData).url),1)]),_:2},1024)]),_:2},1032,["content"])]),_:2},1024)]),_:2},1024)):H("",!0)]),_:2},1024)]),_:1}),e(h,{label:"服务器名",prop:"agent.hostname",sortable:""}),e(h,{label:"最近上报时间",prop:"lastTimestamp",sortable:""}),e(h,{width:"160",label:"状态",prop:"status.statusName",sortable:""},{default:t(a=>[a?(d(),g(u,{key:0,modelValue:a.row.status.statusId,"onUpdate:modelValue":v=>a.row.status.statusId=v,onChange:v=>ne(a.row.id,a.row.status.statusId)},{default:t(()=>[(d(!0),M($,null,F(q.value,v=>(d(),g(p,{label:v.statusName,value:v.statusId,key:v.value},null,8,["label","value"]))),128))]),_:2},1032,["modelValue","onUpdate:modelValue","onChange"])):H("",!0)]),_:1}),e(h,{label:"选项",prop:""},{default:t(a=>[e(ce,null,{dropdown:t(()=>[e(ie,null,{default:t(()=>[e(de,{onClick:v=>A(a.row.id)},{default:t(()=>[i("删除")]),_:2},1032,["onClick"])]),_:2},1024)]),default:t(()=>[e(ue,{style:{cursor:"pointer"}},{default:t(()=>[e(re)]),_:1})]),_:2},1024)]),_:1})]),_:1},8,["data"])]),b("div",$e,[e(_e,{"current-page":w.pageNum,"onUpdate:currentPage":n[2]||(n[2]=a=>w.pageNum=a),"page-size":w.pageSize,"onUpdate:pageSize":n[3]||(n[3]=a=>w.pageSize=a),"page-sizes":[10,20,50,100],small:!1,disabled:Q.value,background:!0,layout:"total, sizes, prev, pager, next, jumper",total:z.value,onSizeChange:ee,onCurrentChange:te},null,8,["current-page","page-size","disabled","total"])])]),_:1})),[[P,C.value]])])}}});const Ye=qe(Fe,[["__scopeId","data-v-a95f5e08"]]);export{Ye as default};
