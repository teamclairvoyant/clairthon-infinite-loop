const menu = [
  {
    icon: 'grid-add-fill-c',
    text: 'Dashboard',
    link: '/HomePage'
  },
  {
    heading: 'Interviewer Section'
  },
  {
    icon: 'users',
    text: 'Manage Interviews',
    active: false,
    subMenu: [
      {
        icon: 'user-list-fill',
        text: 'Interviewer List',
        link: '/interview-list'
      }
    ]
  },
  {
    icon: 'users',
    text: 'List',
    active: false,
    subMenu: [
      {
        icon: 'user-list-fill',
        text: 'List',
        link: '/list'
      }
    ]
  }
];
export default menu;
