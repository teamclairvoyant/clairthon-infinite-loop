/* eslint-disable no-unused-vars */
import React from 'react';
import { render } from '@testing-library/react';
import { ListProvider } from './listContext';
import { request } from '../utils/axiosUtils';
import mockList from '../common/MockData/mockList.json';
jest.mock('../utils/axiosUtils', () => ({
  request: ({ ...options }) => new Promise((resolve, reject) => resolve(mockList))
}));

describe('InterviewList', () => {
  const ChildComponent = () => <p>This is children component</p>;

  test('renders learn List context', () => {
    const { container } = render(
      <ListProvider>
        <ChildComponent />
      </ListProvider>
    );

    expect(container).toBeDefined();
  });
});
