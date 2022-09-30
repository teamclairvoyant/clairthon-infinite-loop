import React, { Suspense, useLayoutEffect } from 'react';
import { Switch, Route } from 'react-router-dom';
import { UserContextProvider } from '../pages/pre-built/interview-manage/UserContext';
import { RedirectAs404 } from '../utils/Utils';
import { ListProvider } from '../context/listContext';
import Homepage from '../pages/Homepage';

import Terms from '../pages/others/Terms';
import InterviewerCalender from '../pages/pre-built/interview-manage/InterviewerCalender';

import InterviewList from '../pages/pre-built/interview-manage/InterviewList';
import List from '../pages/List/List';

const Pages = () => {
  useLayoutEffect(() => {
    window.scrollTo(0, 0);
  });

  return (
    <Suspense fallback={<div />}>
      <Switch>
        <Route
          exact
          path={`${process.env.PUBLIC_URL}/interview-list`}
          render={() => (
            <UserContextProvider>
              <ListProvider>
                <InterviewList />
              </ListProvider>
            </UserContextProvider>
          )}></Route>
        <Route
          exact
          path={`${process.env.PUBLIC_URL}/interviewer-calendar/:id`}
          render={(props) => (
            <UserContextProvider>
              <ListProvider>
                <InterviewerCalender {...props} />
              </ListProvider>
            </UserContextProvider>
          )}></Route>

        <Route
          exact
          path={`${process.env.PUBLIC_URL}/pages/terms-policy`}
          component={Terms}></Route>

        <Route exact path={`${process.env.PUBLIC_URL}/homepage`} component={Homepage}></Route>
        <Route
          exact
          path={`${process.env.PUBLIC_URL}/list`}
          render={() => (
            <ListProvider>
              <List />
            </ListProvider>
          )}></Route>
        <Route
          exact
          path={`${process.env.PUBLIC_URL}/`}
          render={() => (
            <UserContextProvider>
              <ListProvider>
                <InterviewList />
              </ListProvider>
            </UserContextProvider>
          )}></Route>
        <Route component={RedirectAs404}></Route>
      </Switch>
    </Suspense>
  );
};
export default Pages;
