import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/search_album',
    name: 'search_album',
    component: () => import('../views/SearchAlbumView.vue')
  },
  {
    path: '/release/:mbid/details',
    name: 'release_details',
    component: () => import('../views/ReleaseDetailsView.vue')
  },
  {
    path: '/search_artist',
    name: 'search_artist',
    component: () => import('../views/SearchArtistView.vue')
  },
  {
    path: '/about',
    name: 'about',
    component: () => import('../views/AboutView.vue')
  },
  {
    path: '/artist/:mbid/details',
    name: 'artist_details',
    component: () => import('../views/ArtistDetailsView.vue')
  },
  {
    path: '/signup',
    name: 'signup',
    component: () => import('../views/SignupView.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/logout',
    name: 'logout',
    // component: () => import('../views/LoginView.vue')
  },
  {
    path: '/profile',
    name: 'profile',
    component: () => import('../views/ProfileView.vue')
  },
  {
    path: '/my_ratings',
    name: 'my_ratings',
    component: () => import('../views/RatingsView.vue')
  },
  {
    path: '/later_list',
    name: 'later_list',
    component: () => import('../views/LaterListView.vue')
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach((to, from, next) => {
  const publicPages = ['/login', '/signup', '/about', '/search', '/release', '/artist'];
  // const authRequired = !publicPages.includes(to.path);
  var isAuthRequired = true;
  const loggedIn = localStorage.getItem('user_info');
  for (var i in publicPages) {
    let page = publicPages[i]
    if (to.path.startsWith(page)) {
      isAuthRequired = false;
      break;
    }
  } 
  
  // trying to access a restricted page + not logged in
  // redirect to login page
  if (isAuthRequired && !loggedIn && !to.path.endsWith('/') ) {
    next('/login');
  } else {
    next();
  }
});

export default router;
