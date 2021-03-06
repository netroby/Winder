/**
 * Copyright (c) 2016 eBay Software Foundation. All rights reserved.
 *
 * Licensed under the MIT license.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 *
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.ebayopensource.winder;

/**
 * I Winder Job Context
 *
 * @author Sheldon Shao xshao@ebay.com on 10/12/16.
 * @version 1.0
 */
public interface WinderJobContext {

    /**
     * Winder Engine
     *
     * @return
     */
    WinderEngine getEngine();

    /**
     * Return JobId
     *
     * @return
     */
    JobId getJobId();

    /**
     * Return JobId in string.
     * @return
     */
    String getJobIdAsString();

    /**
     * The first time, it will be -1.
     *
     * @return current job step code
     */
    int getJobStep();

    void setJobStep(int step);

    boolean isRecovering();

    String getStatusMessage();

    void setStatusMessage(String msg);

    void setStatusMessage(String msg, Throwable t);

    boolean isAwaitingForAction(boolean isAwaiting);

    void setAwaitingForAction(boolean isAwaiting);

    void done(StatusEnum finalStatus);

    void setError();

    StatusEnum getJobStatus();

    void setJobStatus(StatusEnum status);

    <TI extends TaskInput, TR extends TaskResult> WinderJobSummary<TI, TR> getJobSummary();

    StatusUpdate addUpdate(StatusEnum status, String message);

    StatusUpdate addUpdate(StatusEnum status, String message, Throwable cause);

    JobId[] getChildJobs();

    <TI extends TaskInput, TR extends TaskResult> WinderJobDetail<TI, TR> getJobDetail();

    void updateJobData() throws WinderException;
}
