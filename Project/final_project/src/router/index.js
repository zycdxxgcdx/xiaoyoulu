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
      // children: [{path: 'login',component: Login,meta: {title:'??????'}}],
      {path: 'main',component: MainPage_content,children: [{path: 'login',component: Login,meta: {title:'??????'}}],
        // component:TestLogin,
        meta: {title:'??????'}},
      {path: 'class',component: ClassPage,children: [{path: 'login',component: Login,meta: {title:'??????'}}],meta: {title:'??????'}},
      {path: 'school',component: SchoolPage,children: [{path: 'login',component: Login,meta: {title:'??????'}}],meta: {title:'??????'}},
      {path:'createSchool',component: CreateSchool,meta:{title:'????????????'}},
      {path:'createClass',component: CreateClass,meta:{title:'????????????'}},
      {path: 'followmessages/:userId',component: FollowMessages,children: [{path: 'login',component: Login,meta: {title:'??????'}}],meta: {requireAuth: true,title:'????????????'}},
      {path: 'post',component: Post,meta:{requireAuth: true,title:'??????'}},
      {path:'publicpost/:publicPostId',component: PublicPost,children: [{path: 'login',component: Login,meta: {title:'??????'}}],meta:{title:'??????'}},
      {path:'searchpost',component: SearchPost,children: [{path: 'login',component: Login,meta: {title:'??????'}}],meta:{title:'????????????'}},
      {path:'/u/:userId',component: HomePage,
        children: [{path: 'login',component: Login,meta: {requireAuth: true,title:'??????'}},{path:'/',component:HomeInfo,meta:{requireAuth: true,title:'????????????'}},
          {path:'dynamic',component:MyDynamic,meta:{requireAuth: true,title:'????????????'}},{path:'school',component:MySchool,meta:{requireAuth: true,title:'????????????'}},
          {path:'class',component:MyClass,meta:{requireAuth: true,title:'????????????'}}]},
      {path:'/u/:userId/profile',component:Profile,meta:{requireAuth: true,title:'????????????'}},
      {path: 'dynamic/:dynamicId',component: PersonDynamic,children: [{path: 'login',component: Login,meta: {title:'??????'}}],meta: {requireAuth: true,title:'????????????'}},
      {path:'classinfo',component: ClassInfo,children: [{path: 'login',component: Login,meta: {title:'??????'}}],meta: {requireAuth: true,title:'????????????'}},
      {path:'schoolinfo',component: SchoolInfo,children: [{path: 'login',component: Login,meta: {title:'??????'}}],meta: {title:'????????????'}},
      {path:'message/:userId',component: Message,redirect:'message/:userId/reply/publicpost',
        children: [{path: 'login',component: Login,meta: {title:'??????'}},{path:"reply/publicpost",component:PublicPost_Reply,meta:{requireAuth: true,title:'????????????'}},
          {path:'reply/dynamic',component:Dynamic_Reply,meta:{requireAuth: true,title:'????????????'}},
          {path:'reply/replyinfo',component:ReplyInfo_Reply,meta:{requireAuth: true,title:'????????????'}},
          {path:'classinfo/apply',component:ClassApplyInfo,meta:{requireAuth: true,title:'????????????'}},
          {path:'classinfo/notify',component:ClassNotifyInfo,meta:{requireAuth: true,title:'????????????'}},
          {path:'whisper',component:Whisper,
            children:[{path:':Id',component:WhisperInfo,meta:{requireAuth: true,title:'????????????'}}],meta:{requireAuth: true,title:'????????????'}},
          {path:'likeinfo',component:MyLike,meta:{requireAuth: true,title:'????????????'}}],
        meta: {requireAuth: true,title:'????????????'}}
      ],
  },
  // {path: '/followmessages',component: FollowMessages,meta: {title:'????????????'}},
  {path: '*',redirect: "/404"},
  {path: '/404',component: NotFound,meta:{title:'??????????????????'}},
  // {path: 'post',component: Post}
]

const router = new VueRouter({
  mode: 'history',  //???????????? #
  base: process.env.BASE_URL,
  routes
})
VueRouter.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}
// ????????????????????????title ????????????
router.beforeEach((to,from,next) => {
  //????????????????????????????????????????????? ??????????????????????????????
  if(to.meta.requireAuth){
    if(localStorage.getItem('Authorization')){
      //???????????????????????????????????????????????????
      document.title = to.meta.title;
      next();
    }else{
      ElementUI.Message({
        message: '?????????????????????????????????????????????',
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
//???router???????????????vue??????  ???main.js???
export default router
