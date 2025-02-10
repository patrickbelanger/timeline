// Licensed to the Software Freedom Conservancy (SFC) under one
// or more contributor license agreements.  See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership.  The SFC licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied.  See the License for the
// specific language governing permissions and limitations
// under the License.

import { BrowserRouter, Route, Routes } from "react-router-dom";
import { MantineProvider } from "@mantine/core";
import { theme } from "./core/theme/theme.ts";
import LoginRegistrationLayout from "./core/components/layouts/login-registration-layout.tsx";
import NotFound from "./core/components/containers/errors/not-found.tsx";
import "@mantine/core/styles.css";
import "./App.css";
import LoginForm from "./core/components/forms/login-form.tsx";

// other css files are required only if
// you are using components from the corresponding package
// import '@mantine/dates/styles.css';
// import '@mantine/dropzone/styles.css';
// import '@mantine/code-highlight/styles.css';

function App() {
  return (
    <MantineProvider theme={theme} defaultColorScheme="dark">
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LoginRegistrationLayout />}>
            <Route index element={<LoginForm />} />
          </Route>
          {/*
            <Route path="/" element={<BaseLayout />}></Route>
            <Route path="/login" element={<LoginLayout />}>
              <Route index element={<LoginForm />} />
            </Route>
          */}
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
    </MantineProvider>
  );
}

export default App;
