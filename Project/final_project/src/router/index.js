import Vue from 'vue'
import VueRouter from 'vue-router'
import ElementUI from 'element-ui';

const Login = () => import('../components/Login')
const NotFound = () => import('../components/NotFound')
const Index = () => import('../components/Index')

const MainPage_content = () => import('../components/post/MainPage_content')
const Post = () => import('../components/post/Post')
const PublicPost = () => import('../components/post/PublicPost')
const SearchPost = () => import('../components/post/SearchPost')

const ClassPage = () => import('../components/classes/ClassPage')
const SchoolPage = () => import('../components/School/SchoolPage')
const CreateSchool = () => import('../components/School/CreateSchool')
const CreateClass = () => import('../components/classes/CreateClass')
const ClassInfo = () => import('../components/classes/ClassInfo')
const SchoolInfo = () => import('../components/School/SchoolInfo')

const HomePage = () => import('../components/HomePageInfo/HomePage')
const HomeInfo = () => import('../components/HomePageInfo/FourContent/HomeInfo')
const MyDynamic = () => import('../components/HomePageInfo/FourContent/MyDynamic')
const Profile = () => import('../components/HomePageInfo/Profile')
const MySchool = () => import('../components/HomePageInfo/FourContent/MySchool')
const MyClass = () => import('../components/HomePageInfo/FourContent/MyClass')

const FollowMessages = () => import('../components/dynamic/FollowMessages')
const PersonDynamic = () => import('../components/dynamic/PersonDynamic')

const Message = () => import('../components/message/Message')
const ClassApplyInfo = () => import('../components/message/classesInfo/ClassApplyInfo')
const ClassNotifyInfo = () => import('../components/message/classesInfo/ClassNotifyInfo')
const Whisper = () => import('../components/message/myMessage/Whisper')
const WhisperInfo = () => import('../components/message/myMessage/WhisperInfo')
const MyLike = () => import('../components/message/likeInfo/MyLike')
const PublicPost_Reply = () => import('../components/message/reply/PublicPost_Reply')
const Dynamic_Reply = () => import('../components/message/reply/Dynamic_Reply')
const ReplyInfo_Reply = () => import('../components/message/reply/ReplyInfo_Reply')

const originalPush = VueRouter.prototype.push
Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    component: Index,
    redirect: '/main',
    children:[
      // children: [{path: 'login',component: Login,meta: {title:'登录'}}],
      {path: 'main',component: MainPage_content,children: [{path: 'login',component: Login,meta: {title:'登录'}}],
        // component:TestLogin,
        meta: {title:'首页'}},
      {path: 'class',component: ClassPage,children: [{path: 'login',component: Login,meta: {title:'登录'}}],meta: {title:'班级'}},
      {path: 'school',component: SchoolPage,children: [{path: 'login',component: Login,meta: {title:'登录'}}],meta: {title:'学校'}},
      {path:'createSchool',component: CreateSchool,meta:{title:'学校创建'}},
      {path:'createClass',component: CreateClass,meta:{title:'班级创建'}},
      {path: 'followmessages/:userId',component: FollowMessages,children: [{path: 'login',component: Login,meta: {title:'登录'}}],meta: {requireAuth: true,title:'关注动态'}},
      {path: 'post',component: Post,meta:{requireAuth: true,title:'发帖'}},
      {path:'publicpost/:publicPostId',component: PublicPost,children: [{path: 'login',component: Login,meta: {title:'登录'}}],meta:{title:'帖子'}},
      {path:'searchpost',component: SearchPost,children: [{path: 'login',component: Login,meta: {title:'登录'}}],meta:{title:'搜索帖子'}},
      {path:'/u/:userId',component: HomePage,
        children: [{path: 'login',component: Login,meta: {requireAuth: true,title:'登录'}},{path:'/',component:HomeInfo,meta:{requireAuth: true,title:'个人主页'}},
          {path:'dynamic',component:MyDynamic,meta:{requireAuth: true,title:'个人动态'}},{path:'school',component:MySchool,meta:{requireAuth: true,title:'个人学校'}},
          {path:'class',component:MyClass,meta:{requireAuth: true,title:'个人班级'}}]},
      {path:'/u/:userId/profile',component:Profile,meta:{requireAuth: true,title:'个人资料'}},
      {path: 'dynamic/:dynamicId',component: PersonDynamic,children: [{path: 'login',component: Login,meta: {title:'登录'}}],meta: {requireAuth: true,title:'个人动态'}},
      {path:'classinfo',component: ClassInfo,children: [{path: 'login',component: Login,meta: {title:'登录'}}],meta: {requireAuth: true,title:'班级信息'}},
      {path:'schoolinfo',component: SchoolInfo,children: [{path: 'login',component: Login,meta: {title:'登录'}}],meta: {title:'学校信息'}},
      {path:'message/:userId',component: Message,redirect:'message/:userId/reply/publicpost',
        children: [{path: 'login',component: Login,meta: {title:'登录'}},{path:"reply/publicpost",component:PublicPost_Reply,meta:{requireAuth: true,title:'回复我的'}},
          {path:'reply/dynamic',component:Dynamic_Reply,meta:{requireAuth: true,title:'回复我的'}},
          {path:'reply/replyinfo',component:ReplyInfo_Reply,meta:{requireAuth: true,title:'回复我的'}},
          {path:'classinfo/apply',component:ClassApplyInfo,meta:{requireAuth: true,title:'班级信息'}},
          {path:'classinfo/notify',component:ClassNotifyInfo,meta:{requireAuth: true,title:'班级信息'}},
          {path:'whisper',component:Whisper,
            children:[{path:':Id',component:WhisperInfo,meta:{requireAuth: true,title:'我的消息'}}],meta:{requireAuth: true,title:'我的消息'}},
          {path:'likeinfo',component:MyLike,meta:{requireAuth: true,title:'点赞信息'}}],
        meta: {requireAuth: true,title:'消息中心'}}
      ],
  },
  // {path: '/followmessages',component: FollowMessages,meta: {title:'关注动态'}},
  {path: '*',redirect: "/404"},
  {path: '/404',component: NotFound,meta:{title:'访问路径出错'}},
  // {path: 'post',component: Post}
]

const router = new VueRouter({
  mode: 'history',  //去掉井号 #
  base: process.env.BASE_URL,
  routes
})
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}
// 设置每一个页面的title 全局守卫
router.beforeEach((to,from,next) => {
  //路由跳转时验证当前用户是否登录 未登录跳转至登陆界面
  if(to.meta.requireAuth){
    if(localStorage.getItem('Authorization')){
      //可以检查直接用链接跳转的未登录用户
      document.title = to.meta.title;
      next();
    }else{
      ElementUI.Message({
        message: '未登陆，或登录信息过期，请登录',
        type: 'warning'
      });
      setTimeout(()=>{
        next({path: '/main'});
      },500)
    }
  }else{
    document.title = to.meta.title;
    next();
  }
})
//将router对象传入到vue实例  在main.js中
export default router
