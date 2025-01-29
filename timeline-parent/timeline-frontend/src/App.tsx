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
import { CssBaseline, CssVarsProvider } from "@mui/joy";
import LoginRegistrationLayout from "./core/components/layouts/login-registration-layout.tsx";
import LoginForm from "./core/components/forms/login-form.tsx";
import RegistrationForm from "./core/components/forms/registration-form.tsx";

//const customTheme = extendTheme({ defaultColorScheme: "dark" });

function App() {
  return (
    <CssVarsProvider>
      <CssBaseline>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<LoginRegistrationLayout />}>
              <Route index element={<LoginForm />} />
              <Route path="/registration-form" element={<RegistrationForm />} />
            </Route>
            {/*
          <Route path="/" element={<BaseLayout />}></Route>
          <Route path="/login" element={<LoginLayout />}>
            <Route index element={<LoginForm />} />
          </Route>
          <Route path="*" element={<NotFound />} />
          */}
          </Routes>
        </BrowserRouter>
      </CssBaseline>
    </CssVarsProvider>
  );
}

export default App;
