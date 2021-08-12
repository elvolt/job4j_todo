const taskList = document.querySelector('#task-list');
const form = document.querySelector('form');
const showAllInput = document.querySelector('input[name=showAll]');
const descriptionInput = form.querySelector('input[name=description]');

const state = {
    tasks: [],
    showAllTasks: false,
    formValid: true,
};

const render = () => {
    if (state.formValid === false) {
        descriptionInput.classList.add('is-invalid');
        return;
    } else {
        descriptionInput.classList.remove('is-invalid');
    }

    taskList.innerHTML = '';
    if (state.showAllTasks) {
        state.tasks
            .forEach((task) => {
                if (task.done) {
                    taskList.append(taskItemTemplate(task))
                } else {
                    taskList.prepend(taskItemTemplate(task))
                }
            });
    } else {
        state.tasks
            .filter((task) => !task.done)
            .forEach((task) => taskList.prepend(taskItemTemplate(task)));
    }
};

const taskItemTemplate = (task) => {
    const tr = document.createElement('tr');
    const description = document.createElement('td');
    description.textContent = task.description;

    const completed = document.createElement('td');
    let doneEl;
    if (task.done) {
        doneEl = document.createElement('span');
        doneEl.classList.add('badge', 'bg-success', 'p-2');
        doneEl.textContent = 'Completed';
    } else {
        doneEl = document.createElement('button');
        doneEl.classList.add('btn', 'btn-primary');
        doneEl.setAttribute('data-task-id', task.id);
        doneEl.textContent = 'Done';
    }

    completed.append(doneEl);

    tr.append(description, completed);
    return tr;
};

const getAllTasks = async () => {
    try {
        const response = await axios.get('http://localhost:8080/todo/tasks');
        state.tasks = response.data;
    } catch (error) {
        console.error(error);
    }
};

const addTask = async (description) => {
    try {
        const data = { description };
        const response = await axios.post('http://localhost:8080/todo/tasks', data);
        state.tasks.push(response.data);
    } catch (error) {
        console.error(error);
    }
};

const updateTask = async (task) => {
    try {
        const response = await axios.post('http://localhost:8080/todo/tasks', task);
        const responseTask = response.data;
        const updatedTask = state.tasks.find((task) => task.id === responseTask.id);
        Object.keys(updatedTask).forEach((key) => updatedTask[key] = responseTask[key]);
    } catch (error) {
        console.error(error);
    }
}

const onDocumentLoadHandler = () => getAllTasks().then(render);

const onShowAllClickHandler = (e) => {
    const {target} = e;
    state.showAllTasks = target.checked;
    render();
};

const onFormSubmitHandler = async (e) => {
    e.preventDefault();
    const {target} = e;
    const formData = new FormData(target);
    const description = formData.get('description');

    if (description === '') {
        state.formValid = false;
        render();
        return;
    } else {
        state.formValid = true;
    }

    await addTask(description)
        .then(render);

    form.reset();
    descriptionInput.focus();
};

const onChangeCompletedHandler = (e) => {
    const { target } = e;
    if (!target.hasAttribute('data-task-id')) {
        return;
    }
    const currentTask = state.tasks.find((task) => task.id === parseInt(target.dataset.taskId));
    updateTask({ ...currentTask, done: true })
        .then(render);
};

document.addEventListener('DOMContentLoaded', onDocumentLoadHandler);
showAllInput.addEventListener('change', onShowAllClickHandler);
form.addEventListener('submit', onFormSubmitHandler);
taskList.addEventListener('click', onChangeCompletedHandler);