import React, { Suspense, useLayoutEffect } from "react";
import { Switch, Route } from "react-router-dom";
import { UserContextProvider } from "../pages/pre-built/interview-manage/UserContext";
import { RedirectAs404 } from "../utils/Utils";
import { ListProvider } from "../context/listContext";
import Homepage from "../pages/Homepage";

import Terms from "../pages/others/Terms";
import InterviewerCalender from "../pages/pre-built/interview-manage/InterviewerCalender";

import InterviewList from "../pages/pre-built/interview-manage/InterviewList";

const Pages = () => {
  useLayoutEffect(() => {
    window.scrollTo(0, 0);
  });

  return (
    <Suspense fallback={<div />}>
      <Switch>
        {/*Dashboards*/}

        {/*Pre-built Pages*/}
        <Route //Context Api added
          exact
          path={`${process.env.PUBLIC_URL}/interview-list`}
          render={() => (
            <UserContextProvider>
              <ListProvider>
                <InterviewList />
              </ListProvider>
            </UserContextProvider>
          )}
        ></Route>
        <Route //Context Api added
          exact
          path={`${process.env.PUBLIC_URL}/interviewer-calendar/:id`}
          render={(props) => (
            <UserContextProvider>
              <ListProvider>
                <InterviewerCalender {...props} />
              </ListProvider>
            </UserContextProvider>
          )}
        ></Route>
        {/*Demo Pages*/}
        <Route
          exact
          path={`${process.env.PUBLIC_URL}/pages/terms-policy`}
          component={Terms}
        ></Route>

        <Route
          exact
          path={`${process.env.PUBLIC_URL}/`}
          component={Homepage}
        ></Route>
        <Route component={RedirectAs404}></Route>
      </Switch>
    </Suspense>
  );
};
export default Pages;
